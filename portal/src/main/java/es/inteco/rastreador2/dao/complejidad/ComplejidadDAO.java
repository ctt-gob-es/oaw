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
package es.inteco.rastreador2.dao.complejidad;

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
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;

/**
 * The Class ComplejidadDAO.
 */
public final class ComplejidadDAO {
	/**
	 * Instantiates a new dependencia DAO.
	 */
	private ComplejidadDAO() {
	}

	/**
	 * Count dependencias.
	 *
	 * @param c      the c
	 * @param nombre the nombre
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countComplejidades(Connection c, String nombre) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM complejidades_lista d WHERE 1=1 ";
		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (StringUtils.isNotEmpty(nombre)) {
				ps.setString(count++, "%" + nombre + "%");
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
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
	public static List<ComplejidadForm> getComplejidades(Connection c, String nombre, int page) throws SQLException {
		final List<ComplejidadForm> results = new ArrayList<>();
		String query = "SELECT d.id_complejidad, d.nombre, d.profundidad, d.amplitud FROM complejidades_lista d WHERE 1=1 ";
		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}
		query += "ORDER BY UPPER(d.id_complejidad) ASC ";
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
					ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString("d.id_complejidad"));
					complejidadForm.setName(rs.getString("d.nombre"));
					complejidadForm.setProfundidad(rs.getInt("d.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("d.amplitud"));
					results.add(complejidadForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	public static List<ComplejidadForm> getComplejidadesObs(Connection c, String[] tagsToFiler, String[] exObsIds) throws SQLException {
		final List<ComplejidadForm> results = new ArrayList<>();
		String query = "SELECT DISTINCT cx.id_complejidad, cx.nombre, cx.profundidad, cx.amplitud FROM complejidades_lista cx JOIN lista l ON cx.id_complejidad = l.id_complejidad JOIN rastreo r ON r.semillas=l.id_lista JOIN rastreos_realizados rr ON rr.id_rastreo = r.id_rastreo JOIN semilla_etiqueta se ON se.id_lista=l.id_lista ";
		if (exObsIds != null && exObsIds.length > 0) {
			query = query + "AND id_obs_realizado IN (" + exObsIds[0];
			for (int i = 1; i < exObsIds.length; i++) {
				query = query + "," + exObsIds[i];
			}
			query = query + ")";
		}
		if (tagsToFiler != null && tagsToFiler.length > 0) {
			query = query + " AND ( 1=1 ";
			for (int i = 0; i < tagsToFiler.length; i++) {
				query = query + " OR el.id_etiqueta= ?";
			}
			query = query + ")";
		}
		query += "ORDER BY UPPER(cx.id_complejidad) ASC ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int paramNumber = 1;
			if (tagsToFiler != null && tagsToFiler.length > 0) {
				for (int i = 0; i < tagsToFiler.length; i++) {
					ps.setLong(paramNumber, Long.parseLong(tagsToFiler[i]));
					paramNumber++;
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString("cx.id_complejidad"));
					complejidadForm.setName(rs.getString("cx.nombre"));
					complejidadForm.setProfundidad(rs.getInt("cx.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cx.amplitud"));
					results.add(complejidadForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Exists complejidad.
	 *
	 * @param c           the c
	 * @param complejidad the complejidad
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existsComplejidad(Connection c, ComplejidadForm complejidad) throws SQLException {
		boolean exists = false;
		final String query = "SELECT * FROM complejidades_lista WHERE UPPER(nombre) = UPPER(?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, complejidad.getName());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}

	/**
	 * Gets the by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the by id
	 * @throws SQLException the SQL exception
	 */
	public static ComplejidadForm getById(Connection c, String id) throws SQLException {
		final String query = "SELECT * FROM complejidades_lista WHERE id_complejidad = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, Long.parseLong(id));
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				ComplejidadForm cmp = new ComplejidadForm();
				cmp.setName(result.getString("nombre"));
				cmp.setAmplitud(result.getInt("amplitud"));
				cmp.setProfundidad(result.getInt("profundidad"));
				return cmp;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Save.
	 *
	 * @param c           the c
	 * @param complejidad the complejidad
	 * @throws SQLException the SQL exception
	 */
	public static void save(Connection c, ComplejidadForm complejidad) throws SQLException {
		final String query = "INSERT INTO complejidades_lista(nombre, profundidad, amplitud) VALUES (?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, complejidad.getName());
			ps.setInt(2, complejidad.getProfundidad());
			ps.setInt(3, complejidad.getAmplitud());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update.
	 *
	 * @param c           the c
	 * @param complejidad the complejidad
	 * @throws SQLException the SQL exception
	 */
	public static void update(Connection c, ComplejidadForm complejidad) throws SQLException {
		final String query = "UPDATE complejidades_lista SET nombre = ?, profundidad = ?, amplitud = ? WHERE id_complejidad = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, complejidad.getName());
			ps.setInt(2, complejidad.getProfundidad());
			ps.setInt(3, complejidad.getAmplitud());
			ps.setString(4, complejidad.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete.
	 *
	 * @param c             the c
	 * @param idComplejidad the id complejidad
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, Long idComplejidad) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM complejidades_lista WHERE id_complejidad = ?")) {
			ps.setLong(1, idComplejidad);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the complexity by name.
	 *
	 * @param c              the c
	 * @param complexityName the complexity name
	 * @return the complexity by name
	 * @throws Exception the exception
	 */
	public static ComplejidadForm getComplexityByName(Connection c, String complexityName) throws Exception {
		ComplejidadForm complexity = null;
		String query = "SELECT c.id_complejidad, c.nombre FROM complejidades_lista c WHERE c.nombre = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, complexityName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					complexity = new ComplejidadForm();
					complexity.setId(rs.getString("c.id_complejidad"));
					complexity.setName(rs.getString("c.nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return complexity;
	}

	/**
	 * Gets the complexity by id.
	 *
	 * @param c              the c
	 * @param complexityName the complexity name
	 * @return the complexity by id
	 * @throws Exception the exception
	 */
	public static ComplejidadForm getComplexityById(Connection c, String complexityId) throws Exception {
		ComplejidadForm complexity = null;
		String query = "SELECT * FROM complejidades_lista WHERE id_complejidad = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, complexityId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					complexity = new ComplejidadForm();
					complexity.setId(rs.getString("id_complejidad"));
					complexity.setName(rs.getString("nombre"));
					complexity.setProfundidad(Integer.parseInt(rs.getString("profundidad")));
					complexity.setAmplitud(Integer.parseInt(rs.getString("amplitud")));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return complexity;
	}
}