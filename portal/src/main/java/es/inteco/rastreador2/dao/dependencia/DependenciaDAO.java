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
package es.inteco.rastreador2.dao.dependencia;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.ambito.AmbitoDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class DependenciaDAO.
 */
public final class DependenciaDAO {
	/**
	 * Instantiates a new dependencia DAO.
	 */
	private DependenciaDAO() {
	}

	/**
	 * Count dependencias.
	 *
	 * @param c          the c
	 * @param dependency the dependency
	 * @param tagArr     the tag arr
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countDependencias(Connection c, final DependenciaForm dependency, final String[] tagArr) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM dependencia d WHERE 1=1 ";
		query = appendWhereClauses(dependency, tagArr, query);
		try (PreparedStatement ps = c.prepareStatement(query)) {
			count = fillWhereClauses(dependency, tagArr, ps, count);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the dependencias.
	 *
	 * @param c          the c
	 * @param dependency the dependency
	 * @param tagArr     the tag arr
	 * @param page       the page
	 * @return the dependencias
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getDependencias(Connection c, final DependenciaForm dependency, final String[] tagArr, int page) throws SQLException {
		final List<DependenciaForm> results = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official, d.acronym, a.nombre, a.id_ambito, e.id_etiqueta, e.nombre FROM dependencia d LEFT JOIN ambitos_lista a ON d.id_ambit = a.id_ambito LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag WHERE 1=1 ";
		query = appendWhereClauses(dependency, tagArr, query);
		query += "ORDER BY UPPER(d.nombre) ASC ";
		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;
			count = fillWhereClauses(dependency, tagArr, ps, count);
			if (page != Constants.NO_PAGINACION) {
				PropertiesManager pmgr = new PropertiesManager();
				int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
				int resultFrom = pagSize * page;
				ps.setInt(count++, pagSize);
				ps.setInt(count, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(rs.getString("d.emails"));
					if (rs.getInt("d.send_auto") == 0) {
						dependenciaForm.setSendAuto(false);
					} else {
						dependenciaForm.setSendAuto(true);
					}
					if (rs.getInt("d.official") == 0) {
						dependenciaForm.setOfficial(false);
					} else {
						dependenciaForm.setOfficial(true);
					}
					if (rs.getInt("a.id_ambito") != 0) {
						AmbitoForm ambit = new AmbitoForm();
						ambit.setId(String.valueOf(rs.getInt("a.id_ambito")));
						ambit.setName(rs.getString("a.nombre"));
						dependenciaForm.setAmbito(ambit);
					}
					if (rs.getInt("e.id_etiqueta") != 0) {
						EtiquetaForm tag = new EtiquetaForm();
						tag.setId(rs.getLong("e.id_etiqueta"));
						tag.setName(rs.getString("e.nombre"));
						dependenciaForm.setTag(tag);
					}
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setAcronym(rs.getString("d.acronym"));
					results.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Fill where clauses.
	 *
	 * @param dependency the dependency
	 * @param tagArr     the tag arr
	 * @param ps         the ps
	 * @param count      the count
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	private static int fillWhereClauses(final DependenciaForm dependency, final String[] tagArr, PreparedStatement ps, int count) throws SQLException {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(dependency.getName())) {
			ps.setString(count++, "%" + dependency.getName() + "%");
		}
		if (dependency.getAmbito() != null && !org.apache.commons.lang3.StringUtils.isEmpty(dependency.getAmbito().getId())) {
			ps.setString(count++, dependency.getAmbito().getId());
		}
		if (dependency.getOfficial() != null) {
			ps.setBoolean(count++, dependency.getOfficial());
		}
		if (dependency.getSendAuto() != null) {
			ps.setBoolean(count++, dependency.getSendAuto());
		}
		if (tagArr != null && tagArr.length > 0) {
			for (int i = 0; i < tagArr.length; i++) {
				ps.setLong(count++, Long.parseLong(tagArr[i]));
			}
		}
		return count;
	}

	/**
	 * Append where clauses.
	 *
	 * @param dependency the dependency
	 * @param tagArr     the tag arr
	 * @param query      the query
	 * @return the string
	 */
	private static String appendWhereClauses(final DependenciaForm dependency, final String[] tagArr, String query) {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(dependency.getName())) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}
		if (dependency.getAmbito() != null && !org.apache.commons.lang3.StringUtils.isEmpty(dependency.getAmbito().getId())) {
			query += " AND d.id_ambit = ?  ";
		}
		if (dependency.getOfficial() != null) {
			query += " AND d.official = ?  ";
		}
		if (dependency.getSendAuto() != null) {
			query += " AND d.send_auto = ?  ";
		}
		if (tagArr != null && tagArr.length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < tagArr.length; i++) {
				query = query + " OR d.id_tag= ?";
			}
			query = query + ")";
		}
		return query;
	}

	/**
	 * Find by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the dependencia form
	 * @throws SQLException the SQL exception
	 */
	public static DependenciaForm findById(Connection c, final Long id) throws SQLException {
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official, d.acronym FROM dependencia d WHERE d.id_dependencia = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(rs.getString("d.emails"));
					if (rs.getInt("d.send_auto") == 0) {
						dependenciaForm.setSendAuto(false);
					} else {
						dependenciaForm.setSendAuto(true);
					}
					if (rs.getInt("d.official") == 0) {
						dependenciaForm.setOfficial(false);
					} else {
						dependenciaForm.setOfficial(true);
					}
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setAcronym(rs.getString("d.acronym"));
					return dependenciaForm;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Find by id.
	 *
	 * @param c    the c
	 * @param name the name
	 * @return the dependencia form
	 * @throws SQLException the SQL exception
	 */
	public static DependenciaForm findByName(Connection c, final String name) throws SQLException {
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym,id_ambit, e.id_etiqueta, e.nombre FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag WHERE d.nombre = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(rs.getString("d.emails"));
					if (rs.getInt("d.send_auto") == 0) {
						dependenciaForm.setSendAuto(false);
					} else {
						dependenciaForm.setSendAuto(true);
					}
					if (rs.getInt("d.official") == 0) {
						dependenciaForm.setOfficial(false);
					} else {
						dependenciaForm.setOfficial(true);
					}
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setAcronym(rs.getString("d.acronym"));
					if (rs.getInt("e.id_etiqueta") != 0) {
						EtiquetaForm tag = new EtiquetaForm();
						tag.setId(rs.getLong("e.id_etiqueta"));
						tag.setName(rs.getString("e.nombre"));
						dependenciaForm.setTag(tag);
					}
					if (!org.apache.commons.lang3.StringUtils.isEmpty(rs.getString("d.id_ambit"))) {
						dependenciaForm.setAmbito(AmbitoDAO.getAmbitByID(c, rs.getString("d.id_ambit")));
					}
					return dependenciaForm;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Find not exists or associated.
	 *
	 * @param c                         the c
	 * @param updatedAndNewDependencies the updated and new dependencies
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> findNotExistsAnNotAssociated(Connection c, final List<DependenciaForm> updatedAndNewDependencies) throws SQLException {
		List<DependenciaForm> list = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym,id_ambit,e.id_etiqueta, e.nombre "
				+ "FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag  " + "WHERE d.id_dependencia NOT IN (SELECT sd.id_dependencia FROM semilla_dependencia sd) ";
		if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
			query = query + "AND UPPER(d.nombre) NOT IN (";
			for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
				query = query + "UPPER(?)";
				if (i < updatedAndNewDependencies.size() - 1) {
					query = query + ",";
				}
			}
			query = query + ")";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
				for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
					ps.setString(i + 1, updatedAndNewDependencies.get(i).getName());
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(rs.getString("d.emails"));
					if (rs.getInt("d.send_auto") == 0) {
						dependenciaForm.setSendAuto(false);
					} else {
						dependenciaForm.setSendAuto(true);
					}
					if (rs.getInt("d.official") == 0) {
						dependenciaForm.setOfficial(false);
					} else {
						dependenciaForm.setOfficial(true);
					}
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setAcronym(rs.getString("d.acronym"));
					if (rs.getInt("e.id_etiqueta") != 0) {
						EtiquetaForm tag = new EtiquetaForm();
						tag.setId(rs.getLong("e.id_etiqueta"));
						tag.setName(rs.getString("e.nombre"));
						dependenciaForm.setTag(tag);
					}
					if (!org.apache.commons.lang3.StringUtils.isEmpty(rs.getString("d.id_ambit"))) {
						dependenciaForm.setAmbito(AmbitoDAO.getAmbitByID(c, rs.getString("d.id_ambit")));
					}
					list.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return list;
	}

	/**
	 * Find not exists associated.
	 *
	 * @param c                         the c
	 * @param updatedAndNewDependencies the updated and new dependencies
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> findNotExistsAssociated(Connection c, final List<DependenciaForm> updatedAndNewDependencies) throws SQLException {
		List<DependenciaForm> list = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym,id_ambit,e.id_etiqueta, e.nombre "
				+ "FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag  " + "WHERE d.id_dependencia IN (SELECT sd.id_dependencia FROM semilla_dependencia sd) ";
		if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
			query = query + "AND UPPER(d.nombre) NOT IN (";
			for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
				query = query + "UPPER(?)";
				if (i < updatedAndNewDependencies.size() - 1) {
					query = query + ",";
				}
			}
			query = query + ")";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
				for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
					ps.setString(i + 1, updatedAndNewDependencies.get(i).getName());
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(rs.getString("d.emails"));
					if (rs.getInt("d.send_auto") == 0) {
						dependenciaForm.setSendAuto(false);
					} else {
						dependenciaForm.setSendAuto(true);
					}
					if (rs.getInt("d.official") == 0) {
						dependenciaForm.setOfficial(false);
					} else {
						dependenciaForm.setOfficial(true);
					}
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setAcronym(rs.getString("d.acronym"));
					if (rs.getInt("e.id_etiqueta") != 0) {
						EtiquetaForm tag = new EtiquetaForm();
						tag.setId(rs.getLong("e.id_etiqueta"));
						tag.setName(rs.getString("e.nombre"));
						dependenciaForm.setTag(tag);
					}
					if (!org.apache.commons.lang3.StringUtils.isEmpty(rs.getString("d.id_ambit"))) {
						dependenciaForm.setAmbito(AmbitoDAO.getAmbitByID(c, rs.getString("d.id_ambit")));
					}
					list.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return list;
	}

	/**
	 * Exists dependencia.
	 *
	 * @param c           the c
	 * @param dependencia the dependencia
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existsDependencia(Connection c, DependenciaForm dependencia) throws SQLException {
		boolean exists = false;
		final String query = "SELECT * FROM dependencia WHERE UPPER(nombre) = UPPER(?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}

	/**
	 * Save.
	 *
	 * @param c           the c
	 * @param dependencia the dependencia
	 * @throws SQLException the SQL exception
	 */
	public static void save(Connection c, DependenciaForm dependencia) throws SQLException {
		final String query = "INSERT INTO dependencia(nombre, emails, send_auto, official,id_ambit,id_tag, acronym) VALUES (?,?,?,?,?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ps.setString(2, dependencia.getEmails());
			ps.setBoolean(3, dependencia.getSendAuto());
			ps.setBoolean(4, dependencia.getOfficial());
			if (dependencia.getAmbito() != null && StringUtils.isNotEmpty(dependencia.getAmbito().getId())) {
				ps.setString(5, dependencia.getAmbito().getId());
			} else {
				ps.setString(5, null);
			}
			if (dependencia.getTag() != null && dependencia.getTag().getId() != null) {
				ps.setLong(6, dependencia.getTag().getId());
			} else {
				ps.setString(6, null);
			}
			ps.setString(7, dependencia.getAcronym());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update.
	 *
	 * @param c           the c
	 * @param dependencia the dependencia
	 * @throws SQLException the SQL exception
	 */
	public static void update(Connection c, DependenciaForm dependencia) throws SQLException {
		// d.emails, d.send_auto, d.official
		final String query = "UPDATE dependencia SET nombre = ?, emails = ?, send_auto = ? , official = ?, id_ambit = ?, id_tag = ?, acronym =?   WHERE id_dependencia = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ps.setString(2, dependencia.getEmails());
			ps.setBoolean(3, dependencia.getSendAuto());
			ps.setBoolean(4, dependencia.getOfficial());
			if (dependencia.getAmbito() != null && StringUtils.isNotEmpty(dependencia.getAmbito().getId())) {
				ps.setString(5, dependencia.getAmbito().getId());
			} else {
				ps.setString(5, null);
			}
			if (dependencia.getTag() != null && dependencia.getTag().getId() != null) {
				ps.setLong(6, dependencia.getTag().getId());
			} else {
				ps.setString(6, null);
			}
			ps.setString(7, dependencia.getAcronym());
			ps.setLong(8, dependencia.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete.
	 *
	 * @param c             the c
	 * @param idDependencia the id dependencia
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, Long idDependencia) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM dependencia WHERE id_dependencia = ?")) {
			ps.setLong(1, idDependencia);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete.
	 *
	 * @param c            the c
	 * @param dependencies the dependencies
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, final List<DependenciaForm> dependencies) throws SQLException {
		if (dependencies != null && !dependencies.isEmpty()) {
			for (DependenciaForm dependecy : dependencies) {
				delete(c, dependecy.getId());
			}
		}
	}

	/**
	 * Save or update.
	 *
	 * @param c            the c
	 * @param dependencies the dependencies
	 * @throws SQLException the SQL exception
	 */
	public static void saveOrUpdate(Connection c, final List<DependenciaForm> dependencies) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			for (DependenciaForm dependency : dependencies) {
				// Insert new tags if required
				if (dependency.getTag() != null && dependency.getTag().getId() == null) {
					EtiquetaForm newTag = dependency.getTag();
					if (org.apache.commons.lang3.StringUtils.isNotEmpty(newTag.getName())) {
						PreparedStatement psCreateEtiqueta = c.prepareStatement(
								"INSERT INTO etiqueta(nombre,id_clasificacion) VALUES (?, ?) ON DUPLICATE KEY UPDATE id_etiqueta=LAST_INSERT_ID(id_etiqueta), nombre = ?, id_clasificacion = ?",
								Statement.RETURN_GENERATED_KEYS);
						psCreateEtiqueta.setString(1, newTag.getName());
						psCreateEtiqueta.setString(2, "2");
						psCreateEtiqueta.setString(3, newTag.getName());
						psCreateEtiqueta.setString(4, "2");
						int affectedRowsD = psCreateEtiqueta.executeUpdate();
						if (affectedRowsD == 0) {
							throw new SQLException("Creating user failed, no rows affected.");
						}
						ResultSet generatedKeysD = psCreateEtiqueta.getGeneratedKeys();
						if (generatedKeysD.next()) {
							newTag.setId(generatedKeysD.getLong(1));
						} else {
							throw new SQLException("Creating etiquetas failed, no ID obtained.");
						}
					}
					dependency.setTag(newTag);
				}
				if (dependency.getId() != null) {
					ps = c.prepareStatement("UPDATE dependencia SET  emails = ?, official = ?, id_tag = ?, acronym = ?, id_ambit=?  WHERE id_dependencia = ?");
					ps.setString(1, dependency.getEmails());
					ps.setNull(2, Types.BIGINT);
					if (dependency.getOfficial() != null) {
						ps.setBoolean(2, dependency.getOfficial());
					}
					ps.setNull(3, Types.BIGINT);
					if (dependency.getTag() != null) {
						ps.setLong(3, dependency.getTag().getId());
					}
					ps.setString(4, dependency.getAcronym());
					if (dependency.getAmbito() != null) {
						ps.setString(5, dependency.getAmbito().getId());
					}
					ps.setLong(6, dependency.getId());
					ps.executeUpdate();
				} else {
					ps = c.prepareStatement(
							"INSERT INTO dependencia(nombre, emails, official,id_tag,acronym,id_ambit, send_auto) VALUES (?,?,?,?,?,?, 1) ON DUPLICATE KEY UPDATE emails=?, official=?, id_tag=?, acronym=?, id_ambit=?");
					ps.setString(1, dependency.getName());
					ps.setString(2, dependency.getEmails());
					ps.setNull(3, Types.BIGINT);
					if (dependency.getOfficial() != null) {
						ps.setBoolean(3, dependency.getOfficial());
					}
					ps.setNull(4, Types.BIGINT);
					if (dependency.getTag() != null) {
						ps.setLong(4, dependency.getTag().getId());
					}
					ps.setString(5, dependency.getAcronym());
					ps.setNull(6, Types.BIGINT);
					if (dependency.getAmbito() != null) {
						ps.setString(6, dependency.getAmbito().getId());
					}
					ps.setString(7, dependency.getEmails());
					ps.setBoolean(8, dependency.getOfficial());
					if (dependency.getTag() != null) {
						ps.setLong(9, dependency.getTag().getId());
					} else {
						ps.setNull(9, Types.BIGINT);
					}
					ps.setString(10, dependency.getAcronym());
					ps.setNull(11, Types.BIGINT);
					if (dependency.getAmbito() != null) {
						ps.setString(11, dependency.getAmbito().getId());
					}
					ps.executeUpdate();
				}
			}
			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("Error on", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}
}