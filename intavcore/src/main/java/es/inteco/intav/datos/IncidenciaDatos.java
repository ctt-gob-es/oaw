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
package es.inteco.intav.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import es.inteco.common.logging.Logger;
import es.inteco.intav.comun.Incidencia;

/**
 * The Class IncidenciaDatos.
 */
public final class IncidenciaDatos {
	/**
	 * Instantiates a new incidencia datos.
	 */
	private IncidenciaDatos() {
	}

	/**
	 * Save incidence list.
	 *
	 * @param conn          the conn
	 * @param idAnalisis    the id analisis
	 * @param incidenceList the incidence list
	 */
	public static void saveIncidenceList(final Connection conn, int idAnalisis, final List<Incidencia> incidenceList) {
		long time = System.currentTimeMillis();
		try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tincidencia (COD_COMPROBACION, COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, DES_FUENTE) VALUES (?, ?, ?, ?, ?)")) {
			for (Incidencia incidencia : incidenceList) {
				pstmt.setInt(1, incidencia.getCodigoComprobacion());
				pstmt.setLong(2, idAnalisis);
				pstmt.setInt(3, incidencia.getCodigoLineaFuente());
				pstmt.setInt(4, incidencia.getCodigoColumnaFuente());
				pstmt.setString(5, incidencia.getCodigoFuente());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			Logger.putLog("Tiempo de guardado de incidencias en base de datos: " + (System.currentTimeMillis() - time) + " milisegundos", Evaluator.class, Logger.LOG_LEVEL_INFO);
		} catch (Exception ex) {
			Logger.putLog("Exception: ", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
		}
	}

	/**
	 * Gets the incidencias from analisis id.
	 *
	 * @param conn          the conn
	 * @param idAnalisis    the id analisis
	 * @param getOnlyChecks the get only checks
	 * @return the incidencias from analisis id
	 */
	public static List<Incidencia> getIncidenciasFromAnalisisId(final Connection conn, final long idAnalisis, final boolean getOnlyChecks, final boolean originAnnexes) {
		if (getOnlyChecks) {
			return getIncidenciasIdFromAnalisisId(conn, idAnalisis);
		} else {
			return getIncidenciasFromAnalisisId(conn, idAnalisis, originAnnexes);
		}
	}

	/**
	 * Gets the incidencias id from analisis id.
	 *
	 * @param conn       the conn
	 * @param idAnalisis the id analisis
	 * @return the incidencias id from analisis id
	 */
	private static List<Incidencia> getIncidenciasIdFromAnalisisId(final Connection conn, final long idAnalisis) {
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT COD_COMPROBACION FROM tincidencia WHERE COD_ANALISIS = ?")) {
			pstmt.setLong(1, idAnalisis);
			try (ResultSet rs = pstmt.executeQuery()) {
				final List<Incidencia> incidencias = new ArrayList<>();
				while (rs.next()) {
					final Incidencia incidencia = new Incidencia();
					incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
					incidencias.add(incidencia);
				}
				return incidencias;
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return Collections.emptyList();
		}
	}

	/**
	 * Gets the incidencias from analisis id.
	 *
	 * @param conn       the conn
	 * @param idAnalisis the id analisis
	 * @return the incidencias from analisis id
	 */
	public static List<Incidencia> getIncidenciasFromAnalisisId(final Connection conn, final long idAnalisis, final boolean originAnnexes) {
		String statement = "SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA, DES_FUENTE FROM tincidencia WHERE COD_ANALISIS = ?";
		if (originAnnexes) {
			statement = "SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA  FROM tincidencia WHERE COD_ANALISIS = ?";
		}
		try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
			pstmt.setLong(1, idAnalisis);
			try (ResultSet rs = pstmt.executeQuery()) {
				final List<Incidencia> incidencias = new ArrayList<>();
				while (rs.next()) {
					final Incidencia incidencia = new Incidencia();
					incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
					incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
					incidencia.setCodigoLineaFuente(rs.getInt("COD_LINEA_FUENTE"));
					incidencia.setCodigoColumnaFuente(rs.getInt("COD_COLUMNA_FUENTE"));
					incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
					String codigoFuente = "";
					if (!originAnnexes) {
						if (rs.getString("DES_FUENTE") != null) {
							codigoFuente = rs.getString("DES_FUENTE");
						}
					}
					incidencia.setCodigoFuente(codigoFuente);
					incidencias.add(incidencia);
				}
				return incidencias;
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return Collections.emptyList();
		}
	}

	/**
	 * Gets the incidencias by analisis and comprobacion.
	 *
	 * @param conn           the conn
	 * @param idAnalisis     the id analisis
	 * @param idComprobacion the id comprobacion
	 * @return the incidencias by analisis and comprobacion
	 */
	public static List<Incidencia> getIncidenciasByAnalisisAndComprobacion(final Connection conn, final long idAnalisis, final long idComprobacion) {
		try (PreparedStatement pstmt = conn.prepareStatement(
				"SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA, DES_FUENTE FROM tincidencia WHERE cod_analisis = ? AND cod_comprobacion = ?")) {
			pstmt.setLong(1, idAnalisis);
			pstmt.setLong(2, idComprobacion);
			try (ResultSet rs = pstmt.executeQuery()) {
				final ArrayList<Incidencia> incidencias = new ArrayList<>();
				while (rs.next()) {
					final Incidencia incidencia = new Incidencia();
					incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
					incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
					incidencia.setCodigoLineaFuente(rs.getInt("COD_LINEA_FUENTE"));
					incidencia.setCodigoColumnaFuente(rs.getInt("COD_COLUMNA_FUENTE"));
					incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
					incidencia.setCodigoFuente(rs.getString("DES_FUENTE"));
					incidencias.add(incidencia);
				}
				return incidencias;
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return Collections.emptyList();
		}
	}

	/**
	 * Delete incidencias by analisis and comprobacion.
	 *
	 * @param conn           the conn
	 * @param idAnalisis     the id analisis
	 * @param idComprobacion the id comprobacion
	 */
	public static void deleteIncidenciasByAnalisisAndComprobacion(final Connection conn, final long idAnalisis, final long idComprobacion) {
		try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tincidencia WHERE cod_analisis = ? AND cod_comprobacion = ?")) {
			pstmt.setLong(1, idAnalisis);
			pstmt.setLong(2, idComprobacion);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
		}
	}

	/**
	 * Gets the observatory incidencias from analisis id.
	 *
	 * @param conn       the conn
	 * @param idAnalisis the id analisis
	 * @return the observatory incidencias from analisis id
	 */
	public static List<Incidencia> getObservatoryIncidenciasFromAnalisisId(final Connection conn, final long idAnalisis) {
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT COD_ANALISIS, COD_COMPROBACION, COD_INCIDENCIA FROM tincidencia WHERE COD_ANALISIS = ?")) {
			pstmt.setLong(1, idAnalisis);
			try (ResultSet rs = pstmt.executeQuery()) {
				final ArrayList<Incidencia> incidencias = new ArrayList<>();
				while (rs.next()) {
					final Incidencia incidencia = new Incidencia();
					incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
					incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
					incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
					incidencias.add(incidencia);
				}
				return incidencias;
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return Collections.emptyList();
		}
	}
}
