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
package es.inteco.rastreador2.dao.etiqueta;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.etiquetas.ClasificacionForm;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class EtiquetaDAO.
 */
public final class EtiquetaDAO {
	/**
	 * Instantiates a new etiqueta DAO.
	 */
	private EtiquetaDAO() {
	}

	/**
	 * Count etiqueta.
	 *
	 * @param c      the c
	 * @param nombre the nombre
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countEtiquetas(Connection c, String nombre) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM etiqueta e WHERE 1=1 ";
		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(e.nombre) like UPPER(?) ";
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
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the etiquetas.
	 *
	 * @param c      the c
	 * @param pagina the pagina
	 * @return the tags
	 * @throws SQLException the SQL exceptio
	 */
	public static List<EtiquetaForm> getEtiquetas(Connection c, int pagina) throws SQLException {
		final List<EtiquetaForm> results = new ArrayList<>();
		String query = "SELECT e.id_etiqueta, e.nombre, e.id_clasificacion, c.id_clasificacion, c.nombre"
				+ " FROM etiqueta e LEFT JOIN clasificacion_etiqueta c ON(e.id_clasificacion = c.id_clasificacion) WHERE 1=1  ORDER BY UPPER(c.nombre) ASC, UPPER(e.nombre) ASC";
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		query += " LIMIT ? OFFSET ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			// Pagination
			ps.setLong(1, pagSize);
			ps.setLong(2, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final EtiquetaForm etiquetaForm = new EtiquetaForm();
					etiquetaForm.setId(rs.getLong("e.id_etiqueta"));
					etiquetaForm.setName(rs.getString("e.nombre"));
					final ClasificacionForm clasificacionForm = new ClasificacionForm();
					clasificacionForm.setId(rs.getString("e.id_clasificacion"));
					clasificacionForm.setNombre(rs.getString("c.nombre"));
					etiquetaForm.setClasificacion(clasificacionForm);
					results.add(etiquetaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Gets the etiquetas.
	 *
	 * @param c the c
	 * @return the tags
	 * @throws SQLException the SQL exceptio
	 */
	public static List<EtiquetaForm> getAllEtiquetas(Connection c) throws SQLException {
		final List<EtiquetaForm> results = new ArrayList<>();
		String query = "SELECT e.id_etiqueta, e.nombre, e.id_clasificacion, c.id_clasificacion, c.nombre"
				+ " FROM etiqueta e LEFT JOIN clasificacion_etiqueta c ON(e.id_clasificacion = c.id_clasificacion) WHERE 1=1  ORDER BY UPPER(c.nombre) ASC, UPPER(e.nombre) ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final EtiquetaForm etiquetaForm = new EtiquetaForm();
					etiquetaForm.setId(rs.getLong("e.id_etiqueta"));
					etiquetaForm.setName(rs.getString("e.nombre"));
					final ClasificacionForm clasificacionForm = new ClasificacionForm();
					clasificacionForm.setId(rs.getString("e.id_clasificacion"));
					clasificacionForm.setNombre(rs.getString("c.nombre"));
					etiquetaForm.setClasificacion(clasificacionForm);
					results.add(etiquetaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Exists etiqueta.
	 *
	 * @param c        the c
	 * @param etiqueta the etiqueta
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existsEtiqueta(Connection c, EtiquetaForm etiqueta) throws SQLException {
		boolean exists = false;
		final String query = "SELECT * FROM etiqueta WHERE UPPER(nombre) = UPPER(?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, etiqueta.getName());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}

	/**
	 * Save tag .
	 *
	 * @param c            the c
	 * @param etiquetaForm the etiqueta form
	 * @throws SQLException the SQL exception
	 */
	public static void saveEtiqueta(Connection c, EtiquetaForm etiquetaForm) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement("INSERT INTO etiqueta (nombre, id_clasificacion) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, etiquetaForm.getName());
			ps.setString(2, etiquetaForm.getClasificacion().getId());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Update.
	 *
	 * @param c        the c
	 * @param etiqueta the etiqueta
	 * @throws SQLException the SQL exception
	 */
	public static void updateEtiqueta(Connection c, EtiquetaForm etiqueta) throws SQLException {
		final String query = "UPDATE etiqueta SET nombre = ?, id_clasificacion = ? WHERE id_etiqueta = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, etiqueta.getName());
			ps.setString(2, etiqueta.getClasificacion().getId());
			ps.setLong(3, etiqueta.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete.
	 *
	 * @param c          the c
	 * @param idEtiqueta the id etiqueta
	 * @throws SQLException the SQL exception
	 */
	public static void deleteEtiqueta(Connection c, long idEtiqueta) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM etiqueta WHERE id_etiqueta = ?")) {
			ps.setLong(1, idEtiqueta);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Insert tag.
	 *
	 * @param c             the c
	 * @param nombre        the nombre
	 * @param clasificacion the clasificacion
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static Long insertTag(Connection c, String nombre, String clasificacion) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO etiqueta (nombre, id_clasificacion) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, nombre);
			if (StringUtils.isNotEmpty(clasificacion)) {
				ps.setString(2, clasificacion);
			} else {
				ps.setString(2, null);
			}
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL_EXCEPTION:", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the tag clasifications.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the tag clasifications
	 * @throws SQLException the SQL exception
	 */
	public static List<ClasificacionForm> getTagClassifications(Connection c, int page) throws SQLException {
		final List<ClasificacionForm> classifications = new ArrayList<>();
		final String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT * FROM clasificacion_etiqueta ORDER BY nombre ASC";
		} else {
			query = "SELECT * FROM clasificacion_etiqueta ORDER BY nombre ASC LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (page != Constants.NO_PAGINACION) {
				final PropertiesManager pmgr = new PropertiesManager();
				final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
				final int resultFrom = pagSize * page;
				ps.setInt(1, pagSize);
				ps.setInt(2, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ClasificacionForm clasificacionForm = new ClasificacionForm();
					clasificacionForm.setId(rs.getString("id_clasificacion"));
					clasificacionForm.setNombre(rs.getString("nombre"));
					classifications.add(clasificacionForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL_EXCEPTION: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return classifications;
	}

	/**
	 * Count tag classifications.
	 *
	 * @param c the c
	 * @return the integer
	 * @throws Exception the exception
	 */
	public static Integer countTagClassifications(Connection c) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) as numClassifications FROM clasificaciones_etiqueta"); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("numClassifications");
			}
		} catch (Exception e) {
			Logger.putLog("SQL_EXCEPTION:", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the clasificacion by name.
	 *
	 * @param c       the c
	 * @param tagName the tag name
	 * @return the clasificacion by name
	 * @throws Exception the exception
	 */
	public static long getClasificacionByName(Connection c, String tagName) throws Exception {
		long clasificacion = 0;
		String query = "SELECT c.id_clasificacion FROM etiqueta c WHERE c.nombre = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, tagName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					clasificacion = Long.parseLong(rs.getString("c.id_clasificacion"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return clasificacion;
	}

	/**
	 * Gets the id by name.
	 *
	 * @param c       the c
	 * @param tagName the tag name
	 * @return the id by name
	 * @throws Exception the exception
	 */
	public static long getIdByName(Connection c, String tagName) throws Exception {
		long id = 0;
		String query = "SELECT c.id_etiqueta FROM etiqueta c WHERE c.nombre = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, tagName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					id = Long.parseLong(rs.getString("c.id_etiqueta"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return id;
	}

	/**
	 * Gets the by name.
	 *
	 * @param c       the c
	 * @param tagName the tag name
	 * @return the by name
	 * @throws Exception the exception
	 */
	public static EtiquetaForm getByName(Connection c, String tagName) throws Exception {
		EtiquetaForm tag = null;
		String query = "SELECT c.id_etiqueta, c.nombre FROM etiqueta c WHERE c.nombre = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, tagName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					tag = new EtiquetaForm();
					tag.setId(Long.parseLong(rs.getString("c.id_etiqueta")));
					tag.setName(rs.getString("c.nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return tag;
	}

	/**
	 * Gets the by id.
	 *
	 * @param c     the c
	 * @param idTag the id tag
	 * @return the by id
	 * @throws SQLException the SQL exception
	 */
	public static EtiquetaForm getById(Connection c, final int idTag) throws SQLException {
		EtiquetaForm tag = null;
		String query = "SELECT c.id_etiqueta, c.nombre FROM etiqueta c WHERE 1=1 AND c.id_etiqueta = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setInt(1, idTag);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					tag = new EtiquetaForm();
					tag.setId(Long.parseLong(rs.getString("c.id_etiqueta")));
					tag.setName(rs.getString("c.nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return tag;
	}

	/**
	 * Gets the by id.
	 *
	 * @param c             the c
	 * @param idsTags       the ids tags
	 * @param clasification the clasification
	 * @return the by id
	 * @throws SQLException the SQL exception
	 */
	public static List<EtiquetaForm> getByIdsAndClasification(Connection c, final String[] idsTags, final int clasification) throws SQLException {
		List<EtiquetaForm> tags = new ArrayList<>();
		String query = "SELECT c.id_etiqueta, c.nombre FROM etiqueta c WHERE 1=1 AND c.id_clasificacion = ?";
		if (idsTags != null && idsTags.length > 0) {
			query += " AND c.id_etiqueta IN (";
			for (int i = 0; i < idsTags.length; i++) {
				if (!org.apache.commons.lang3.StringUtils.isEmpty(idsTags[i])) {
					query += "?";
					if (i < idsTags.length - 1) {
						query += ",";
					}
				}
			}
			query += ")";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int paramIndex = 1;
			ps.setInt(paramIndex, clasification);
			paramIndex++;
			if (idsTags != null && idsTags.length > 0) {
				for (int i = 0; i < idsTags.length; i++) {
					if (!org.apache.commons.lang3.StringUtils.isEmpty(idsTags[i])) {
						ps.setInt(paramIndex, Integer.parseInt(idsTags[i]));
						paramIndex++;
					}
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					EtiquetaForm tag = new EtiquetaForm();
					tag.setId(Long.parseLong(rs.getString("c.id_etiqueta")));
					tag.setName(rs.getString("c.nombre"));
					tags.add(tag);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return tags;
	}

	/**
	 * Gets the ids fixed tags.
	 *
	 * @param c the c
	 * @return the ids fixed tags
	 * @throws Exception the exception
	 */
	public static List<String> getIdsFixedTags(Connection c) throws Exception {
		List<String> ids = new ArrayList<>();
		String query = "SELECT c.id_etiqueta FROM etiqueta c WHERE c.id_clasificacion = 3 AND (UPPER(c.nombre) = 'FIJA' OR UPPER(c.nombre) = 'FIXED' OR UPPER(c.nombre) = 'FIJO')";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ids.add(rs.getString("c.id_etiqueta"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ids;
	}

	/**
	 * Gets the all etiquetas clasification.
	 *
	 * @param c             the c
	 * @param clasification the clasification
	 * @return the all etiquetas clasification
	 * @throws SQLException the SQL exception
	 */
	public static List<EtiquetaForm> getAllEtiquetasClasification(Connection c, final int clasification) throws SQLException {
		final List<EtiquetaForm> results = new ArrayList<>();
		String query = "SELECT e.id_etiqueta, e.nombre, e.id_clasificacion, c.id_clasificacion, c.nombre"
				+ " FROM etiqueta e LEFT JOIN clasificacion_etiqueta c ON(e.id_clasificacion = c.id_clasificacion) WHERE c.id_clasificacion = ? ORDER BY UPPER(c.nombre) ASC, UPPER(e.nombre) ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setInt(1, clasification);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final EtiquetaForm etiquetaForm = new EtiquetaForm();
					etiquetaForm.setId(rs.getLong("e.id_etiqueta"));
					etiquetaForm.setName(rs.getString("e.nombre"));
					final ClasificacionForm clasificacionForm = new ClasificacionForm();
					clasificacionForm.setId(rs.getString("e.id_clasificacion"));
					clasificacionForm.setNombre(rs.getString("c.nombre"));
					etiquetaForm.setClasificacion(clasificacionForm);
					results.add(etiquetaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}
}