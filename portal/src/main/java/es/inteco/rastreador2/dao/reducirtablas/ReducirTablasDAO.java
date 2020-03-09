/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.dao.reducirtablas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.observatorio.ReducirTablasForm;

/**
 * The Class ReducirTablasDAO.
 */
public final class ReducirTablasDAO {
	/**
	 * Instantiates a new reducir tablas DAO.
	 */
	private ReducirTablasDAO() {
	}

	/**
	 * Reduce.
	 *
	 * @param c                       the c
	 * @param idObservatorioRealizado the id observatorio realizado
	 * @throws SQLException the SQL exception
	 */
	public static void eliminarTablas(Connection c, int idObservatorioRealizado) throws SQLException {
		String query = "SELECT * FROM rastreos_realizados rr WHERE id_obs_realizado = ?";
		Long idRastreoRealizado;
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setInt(1, idObservatorioRealizado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) { // iterando por los id de rastreos_realizados
					idRastreoRealizado = rs.getLong("id"); // 9
					query = "SELECT * FROM `tanalisis` WHERE `cod_rastreo`= ?";
					Long codAnalisis;
					try (PreparedStatement ps2 = c.prepareStatement(query)) {
						ps2.setLong(1, idRastreoRealizado);
						try (ResultSet rs2 = ps2.executeQuery()) {
							while (rs2.next()) { // iterando por los cod_analisis de tanalisis
								codAnalisis = rs2.getLong("cod_analisis"); // 3652
								PreparedStatement deleteAnalisisCSS = c.prepareStatement("UPDATE tanalisis_css SET codigo = NULL WHERE cod_analisis = ?");
								deleteAnalisisCSS.setLong(1, codAnalisis);
								deleteAnalisisCSS.executeUpdate();
//								PreparedStatement deleteIncidencia = c.prepareStatement("DELETE FROM tincidencia WHERE cod_analisis = ?");
//								deleteIncidencia.setLong(1, codAnalisis);
//								deleteIncidencia.executeUpdate();
							}
						}
					}
					PreparedStatement deleteAnalisis = c.prepareStatement("UPDATE tanalisis SET cod_fuente = NULL WHERE cod_rastreo = ?");
					deleteAnalisis.setLong(1, idRastreoRealizado);
					deleteAnalisis.executeUpdate();
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count tablas.
	 *
	 * @param c the c
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countTablas(Connection c) throws SQLException {
		String query = "SELECT COUNT(TABLE_NAME) AS 'Table', ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024) AS 'MB' FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'oaw_js' && ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024)>'5' && TABLE_NAME IN ('tanalisis', 'tanalisis_css', 'tincidiencia') ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the tablas.
	 *
	 * @param c the c
	 * @return the tags
	 * @throws SQLException the SQL exceptio
	 */
	public static List<ReducirTablasForm> getTablas(Connection c) throws SQLException {
		final List<ReducirTablasForm> results = new ArrayList<>();
		String query = "SELECT TABLE_NAME AS 'Table', ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024) AS 'MB' FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'oaw_js' && ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024)>'5' && TABLE_NAME IN ('tanalisis', 'tanalisis_css', 'tincidiencia') ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC";
		// `Table`
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ReducirTablasForm reducirTablasForm = new ReducirTablasForm();
					reducirTablasForm.setName(rs.getString("Table"));
					reducirTablasForm.setSize(rs.getLong("MB"));
					results.add(reducirTablasForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Reduce.
	 *
	 * @param c         the c
	 * @param nameTabla the name tabla
	 * @throws SQLException the SQL exception
	 */
	public static void reduceTabla(Connection c, String nameTabla) throws SQLException {
		String query = "SELECT TABLE_NAME AS 'Table', ROUND((DATA_LENGTH + INDEX_LENGTH)) AS 'Bytes' FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'oaw_js' && TABLE_NAME = ? ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC";
		int size, rows, sizePerRow, memoryToFree, rowsToDelete, id;
		int reducePercentage = 10;
		String nameId;
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, nameTabla);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					size = rs.getInt(2);
					query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'oaw_js' AND TABLE_NAME = ? LIMIT 1";
					try (PreparedStatement ps4 = c.prepareStatement(query)) {
						ps4.setString(1, nameTabla);
						try (ResultSet rs4 = ps4.executeQuery()) {
							if (rs4.next()) {
								nameId = rs4.getString(1);
								query = "SELECT COUNT(*) FROM " + nameTabla + " WHERE 1";
								try (PreparedStatement ps2 = c.prepareStatement(query)) {
									try (ResultSet rs2 = ps2.executeQuery()) {
										if (rs2.next()) {
											rows = rs2.getInt(1);
											sizePerRow = size / rows;
											memoryToFree = size * reducePercentage / 100;
											rowsToDelete = memoryToFree / sizePerRow;
											id = getFirstId(c, nameTabla, nameId);
											for (int i = 0; i < rowsToDelete; i++) {
												if (existsId(c, nameTabla, id, nameId)) {
													try (PreparedStatement ps3 = c.prepareStatement("DELETE FROM " + nameTabla + " WHERE " + nameId + "= ?")) {
														ps3.setInt(1, id);
														ps3.executeUpdate();
													} catch (SQLException e) {
														Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
														throw e;
													}
													id++;
												} else {
													id++;
												}
											}
										}
									}
								} catch (SQLException e) {
									Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
									throw e;
								}
							}
						}
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the first id of table.
	 *
	 * @param c         the c
	 * @param nameTabla the name tabla
	 * @param nameID    the name ID
	 * @return the tags
	 * @throws SQLException the SQL exception
	 */
	public static int getFirstId(Connection c, String nameTabla, String nameID) throws SQLException {
		String query = "SELECT " + nameID + " FROM " + nameTabla + " ORDER BY " + nameID + " LIMIT 1";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			// ps.setString(1, nameTabla);
			// ps.executeUpdate();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return -1;
	}

	/**
	 * Checks if the id exists.
	 *
	 * @param c         the c
	 * @param nameTabla the name tabla
	 * @param id        the id
	 * @param nameID    the name ID
	 * @return the tags
	 * @throws SQLException the SQL exception
	 */
	public static boolean existsId(Connection c, String nameTabla, int id, String nameID) throws SQLException {
		boolean exists = false;
		final String query = "SELECT * FROM " + nameTabla + " WHERE " + nameID + " = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			// ps.setString(1, nameTabla);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ReducirTablasDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}
}