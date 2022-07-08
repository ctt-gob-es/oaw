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
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.observatorio.DataBaseDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class DependenciaDAO.
 */
public final class DependenciaDAO extends DataBaseDAO {
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
	 * @throws Exception the SQL exception
	 */
	public static int countDependencias(Connection c, final DependenciaForm dependency, final String[] tagArr) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
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
	 * @throws Exception the SQL exception
	 */
	public static List<DependenciaForm> getDependencias(Connection c, final DependenciaForm dependency, final String[] tagArr, int page) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<DependenciaForm> results = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official, d.acronym, e.id_etiqueta, e.nombre FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag WHERE 1=1 ";
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
					dependenciaForm.setEmails(formatEmails(rs.getString("d.emails")));
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
					loadAmbits(c, dependenciaForm);
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
	 * Load ambits.
	 *
	 * @param c               the c
	 * @param dependenciaForm the dependencia form
	 * @throws Exception the SQL exception
	 */
	private static void loadAmbits(Connection c, DependenciaForm dependenciaForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement psDependencias = c.prepareStatement(
				"SELECT a.nombre, a.id_ambito FROM ambitos_lista a WHERE a.id_ambito in (SELECT id_ambito FROM dependencia_ambito WHERE id_dependencia = ?) ORDER BY UPPER(a.nombre)");
		psDependencias.setLong(1, dependenciaForm.getId());
		List<AmbitoForm> listAmbits = new ArrayList<>();
		ResultSet rsAmbits = null;
		try {
			rsAmbits = psDependencias.executeQuery();
			while (rsAmbits.next()) {
				AmbitoForm ambit = new AmbitoForm();
				ambit.setId(String.valueOf(rsAmbits.getInt("a.id_ambito")));
				ambit.setName(rsAmbits.getString("a.nombre"));
				listAmbits.add(ambit);
			}
			dependenciaForm.setAmbitos(listAmbits);
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(psDependencias, rsAmbits);
		}
	}

	/**
	 * Fill where clauses.
	 *
	 * @param dependency the dependency
	 * @param tagArr     the tag arr
	 * @param ps         the ps
	 * @param count      the count
	 * @return the int
	 * @throws Exception the SQL exception
	 */
	private static int fillWhereClauses(final DependenciaForm dependency, final String[] tagArr, PreparedStatement ps, int count) throws Exception {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(dependency.getName())) {
			ps.setString(count++, "%" + dependency.getName() + "%");
		}
		if (dependency.getAmbitos() != null && !dependency.getAmbitos().isEmpty()) {
			if (dependency.getAmbitos().size() == 1 && "0".equalsIgnoreCase(dependency.getAmbitos().get(0).getId())) {
			} else {
				for (int i = 0; i < dependency.getAmbitos().size(); i++) {
					ps.setString(count++, dependency.getAmbitos().get(i).getId());
				}
			}
		}
		if (dependency.getOfficialSearch() != null) {
			if (dependency.getOfficialSearch() != 3) {
				if (dependency.getOfficialSearch() == 1) {
					ps.setBoolean(count++, true);
				}
				if (dependency.getOfficialSearch() == 0) {
					ps.setBoolean(count++, false);
				}
			}
		}
		if (dependency.getOfficialSearch() != null) {
			if (dependency.getSendAutoSearch() != 3) {
				if (dependency.getSendAutoSearch() == 1) {
					ps.setBoolean(count++, true);
				}
				if (dependency.getSendAutoSearch() == 0) {
					ps.setBoolean(count++, false);
				}
			}
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
		if (dependency.getAmbitos() != null && !dependency.getAmbitos().isEmpty()) {
			if (dependency.getAmbitos().size() == 1 && "0".equalsIgnoreCase(dependency.getAmbitos().get(0).getId())) {
				query += " AND d.id_dependencia NOT IN (SELECT da.id_dependencia FROM dependencia_ambito da) ";
			} else {
				query += " AND d.id_dependencia IN (SELECT da.id_dependencia FROM dependencia_ambito da WHERE da.id_ambito IN (";
				for (int i = 0; i < dependency.getAmbitos().size(); i++) {
					query += "?";
					if (i < dependency.getAmbitos().size() - 1) {
						query += ",";
					}
				}
				query += "))";
			}
		}
		// Multiple values
		if (dependency.getOfficialSearch() != null) {
			if (dependency.getOfficialSearch() == 3) {
				query += " AND d.official IS NULL  ";
			} else if (dependency.getOfficialSearch() != 2) {
				query += " AND d.official = ?  ";
			}
		}
		if (dependency.getSendAutoSearch() != null) {
			if (dependency.getSendAutoSearch() == 3) {
				query += " AND d.send_auto IS NULL  ";
			} else if (dependency.getSendAutoSearch() != 2) {
				query += " AND d.send_auto = ?  ";
			}
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
	 * @throws Exception the SQL exception
	 */
	public static DependenciaForm findById(Connection c, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official, d.acronym FROM dependencia d WHERE d.id_dependencia = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(formatEmails(rs.getString("d.emails")));
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
	 * @throws Exception the SQL exception
	 */
	public static DependenciaForm findByName(Connection c, final String name) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym, e.id_etiqueta, e.nombre FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag WHERE d.nombre = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(formatEmails(rs.getString("d.emails")));
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
					loadAmbits(c, dependenciaForm);
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
	 * @throws Exception the SQL exception
	 */
	public static List<DependenciaForm> findNotExistsAnNotAssociated(Connection c, final List<DependenciaForm> updatedAndNewDependencies) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<DependenciaForm> list = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym,e.id_etiqueta, e.nombre "
				+ "FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag  " + "WHERE d.id_dependencia NOT IN (SELECT sd.id_dependencia FROM semilla_dependencia sd) ";
		if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
			query = query + "AND UPPER(d.nombre) NOT IN (";
			for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
				query = query + "UPPER(?)";
				if (i < updatedAndNewDependencies.size() - 1) {
					query = query + ",";
				}
			}
			query = query + ") AND d.official =  ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
				int i = 0;
				for (i = 0; i < updatedAndNewDependencies.size(); i++) {
					ps.setString(i + 1, updatedAndNewDependencies.get(i).getName());
				}
				ps.setBoolean(i + 1, updatedAndNewDependencies.get(0).getOfficial());
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(formatEmails(rs.getString("d.emails")));
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
					loadAmbits(c, dependenciaForm);
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
	 * @throws Exception the SQL exception
	 */
	public static List<DependenciaForm> findNotExistsAssociated(Connection c, final List<DependenciaForm> updatedAndNewDependencies) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<DependenciaForm> list = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official,d.acronym,e.id_etiqueta, e.nombre "
				+ "FROM dependencia d LEFT JOIN etiqueta e ON e.id_etiqueta = d.id_tag  " + "WHERE d.id_dependencia IN (SELECT sd.id_dependencia FROM semilla_dependencia sd) ";
		if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
			query = query + "AND UPPER(d.nombre) NOT IN (";
			for (int i = 0; i < updatedAndNewDependencies.size(); i++) {
				query = query + "UPPER(?)";
				if (i < updatedAndNewDependencies.size() - 1) {
					query = query + ",";
				}
			}
			query = query + ") AND d.official =  ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (updatedAndNewDependencies != null && !updatedAndNewDependencies.isEmpty()) {
				int i = 0;
				for (i = 0; i < updatedAndNewDependencies.size(); i++) {
					ps.setString(i + 1, updatedAndNewDependencies.get(i).getName());
				}
				ps.setBoolean(i + 1, updatedAndNewDependencies.get(0).getOfficial());
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));
					dependenciaForm.setEmails(formatEmails(rs.getString("d.emails")));
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
					loadAmbits(c, dependenciaForm);
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
	 * @throws Exception the SQL exception
	 */
	public static boolean existsDependencia(Connection c, DependenciaForm dependencia) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
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
	 * @throws Exception the SQL exception
	 */
	public static void save(Connection c, DependenciaForm dependencia) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final String query = "INSERT INTO dependencia(nombre, emails, send_auto, official,id_tag, acronym) VALUES (?,?,?,?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, dependencia.getName());
			ps.setString(2, dependencia.getEmails());
			if (dependencia.getSendAuto() != null) {
				ps.setBoolean(3, dependencia.getSendAuto());
			} else {
				ps.setNull(3, Types.BOOLEAN);
			}
			if (dependencia.getOfficial() != null) {
				ps.setBoolean(4, dependencia.getOfficial());
			} else {
				ps.setNull(4, Types.BOOLEAN);
			}
			if (dependencia.getTag() != null && dependencia.getTag().getId() != null) {
				ps.setLong(5, dependencia.getTag().getId());
			} else {
				ps.setString(5, null);
			}
			ps.setString(6, dependencia.getAcronym());
			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			insertAmbits(c, dependencia, generatedKeys);
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Insert ambits.
	 *
	 * @param c             the c
	 * @param dependencia   the dependencia
	 * @param generatedKeys the generated keys
	 * @throws Exception the SQL exception
	 */
	private static void insertAmbits(Connection c, DependenciaForm dependencia, ResultSet generatedKeys) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		if (generatedKeys.next()) {
			dependencia.setId(generatedKeys.getLong(1));
			// Inserción de las nuevas
			if (dependencia.getAmbitos() != null && !dependencia.getAmbitos().isEmpty()) {
				StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO dependencia_ambito(id_ambito, id_dependencia) VALUES ");
				for (int i = 0; i < dependencia.getAmbitos().size(); i++) {
					AmbitoForm currentAmbit = dependencia.getAmbitos().get(i);
					slqInsertSemillaDependencia.append("(").append(currentAmbit.getId()).append(",").append(dependencia.getId()).append(")");
					if (i < dependencia.getAmbitos().size() - 1) {
						slqInsertSemillaDependencia.append(",");
					}
				}
				PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
				psInsertarSemillaDependencia.executeUpdate();
			}
		} else {
			throw new SQLException("Creating dependencias or tags failed, no ID obtained.");
		}
	}

	/**
	 * Update.
	 *
	 * @param c           the c
	 * @param dependencia the dependencia
	 * @throws Exception the SQL exception
	 */
	public static void update(Connection c, DependenciaForm dependencia) throws Exception {
		final String query = "UPDATE dependencia SET nombre = ?, emails = ?, send_auto = ? , official = ?, id_tag = ?, acronym =?   WHERE id_dependencia = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ps.setString(2, dependencia.getEmails());
			ps.setBoolean(3, dependencia.getSendAuto());
			ps.setBoolean(4, dependencia.getOfficial());
			if (dependencia.getTag() != null && dependencia.getTag().getId() != null) {
				ps.setLong(5, dependencia.getTag().getId());
			} else {
				ps.setString(5, null);
			}
			ps.setString(6, dependencia.getAcronym());
			ps.setLong(7, dependencia.getId());
			ps.executeUpdate();
			updateDependencyAmbits(c, dependencia);
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update dependency ambits.
	 *
	 * @param c           the c
	 * @param dependencia the dependencia
	 * @throws Exception the SQL exception
	 */
	private static void updateDependencyAmbits(Connection c, DependenciaForm dependencia) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		// Update ambits
		// Borrar las relaciones (no se pueden crear FK a lista por MyISAM no
		// lo permite
		try {
			PreparedStatement deleteSemillaDependencia = c.prepareStatement("DELETE FROM dependencia_ambito WHERE id_dependencia = ?");
			deleteSemillaDependencia.setLong(1, dependencia.getId());
			deleteSemillaDependencia.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error al eliminar las dependencias previas", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		// Inserción de las nuevas
		if (dependencia.getAmbitos() != null && !dependencia.getAmbitos().isEmpty()) {
			StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO dependencia_ambito(id_ambito, id_dependencia) VALUES ");
			for (int i = 0; i < dependencia.getAmbitos().size(); i++) {
				AmbitoForm currentAmbit = dependencia.getAmbitos().get(i);
				slqInsertSemillaDependencia.append("(").append(currentAmbit.getId()).append(",").append(dependencia.getId()).append(")");
				if (i < dependencia.getAmbitos().size() - 1) {
					slqInsertSemillaDependencia.append(",");
				}
			}
			PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
			psInsertarSemillaDependencia.executeUpdate();
		}
	}

	/**
	 * Delete.
	 *
	 * @param c             the c
	 * @param idDependencia the id dependencia
	 * @throws Exception the SQL exception
	 */
	public static void delete(Connection c, Long idDependencia) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
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
	 * @throws Exception the SQL exception
	 */
	public static void delete(Connection c, final List<DependenciaForm> dependencies) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		if (dependencies != null && !dependencies.isEmpty()) {
			String query = "DELETE FROM dependencia WHERE 1=1";
			query = query + " AND id_dependencia IN (" + dependencies.get(0).getId();
			for (int i = 1; i < dependencies.size(); i++) {
				query = query + "," + dependencies.get(i).getId();
			}
			query = query + ")";
			try (PreparedStatement ps = c.prepareStatement(query)) {
				int affectedRowsD = ps.executeUpdate();
				if (affectedRowsD == 0) {
					throw new SQLException("Error deleting.");
				} else {
					Logger.putLog("Deleted " + affectedRowsD + " dependencies", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR);
				}
				c.commit();
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
	}

	/**
	 * Save or update.
	 *
	 * @param c            the c
	 * @param dependencies the dependencies
	 * @throws Exception the SQL exception
	 */
	public static void saveOrUpdate(Connection c, final List<DependenciaForm> dependencies) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
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
					ps = c.prepareStatement("UPDATE dependencia SET  emails = ?, official = ?, id_tag = ?, acronym = ? WHERE id_dependencia = ?");
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
					updateDependencyAmbits(c, dependency);
					ps.setLong(5, dependency.getId());
					ps.executeUpdate();
				} else {
					ps = c.prepareStatement(
							"INSERT INTO dependencia(nombre, emails, official,id_tag,acronym, send_auto) VALUES (?,?,?,?,?, 1) ON DUPLICATE KEY UPDATE emails=?, official=?, id_tag=?, acronym=?");
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
					ps.setString(6, dependency.getEmails());
					ps.setBoolean(7, dependency.getOfficial());
					if (dependency.getTag() != null) {
						ps.setLong(8, dependency.getTag().getId());
					} else {
						ps.setNull(8, Types.BIGINT);
					}
					ps.setString(9, dependency.getAcronym());
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

	/**
	 * Email formatter
	 * 
	 * @param emails
	 * @return Formatted emails
	 */
	private static String formatEmails(String emails) {
		String formattedEmails = "";
		if (emails != null) {
			formattedEmails = emails.trim().replaceAll("\\s+", "");
		}
		return formattedEmails;
	}
}