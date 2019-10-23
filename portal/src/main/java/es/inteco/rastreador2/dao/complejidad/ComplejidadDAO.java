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
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

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
	 * @param c
	 *            the c
	 * @param nombre
	 *            the nombre
	 * @return the int
	 * @throws SQLException
	 *             the SQL exception
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
	 * @param c
	 *            the c
	 * @param nombre
	 *            the nombre
	 * @param page
	 *            the page
	 * @return the complejidades
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static List<ComplejidadForm> getComplejidades(Connection c, String nombre, int page) throws SQLException {
		final List<ComplejidadForm> results = new ArrayList<>();
		String query = "SELECT d.id_complejidad, d.nombre, d.profundidad, d.amplitud FROM complejidades_lista d WHERE 1=1 ";

		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}

		query += "ORDER BY UPPER(d.nombre) ASC ";

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
					complejidadForm.setId(rs.getString("c.id_complejidad"));
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

	/**
	 * Exists complejidad.
	 *
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the complejidad
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
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
	 * Save.
	 *
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the complejidad
	 * @throws SQLException
	 *             the SQL exception
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
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the complejidad
	 * @throws SQLException
	 *             the SQL exception
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
	 * @param c
	 *            the c
	 * @param idDependencia
	 *            the id complejidad
	 * @throws SQLException
	 *             the SQL exception
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

}