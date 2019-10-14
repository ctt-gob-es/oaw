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
package es.inteco.rastreador2.dao.semilla;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.action.semillas.SeedUtils;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.actionform.semillas.UpdateListDataForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.DAOUtils;


/**
 * The Class SemillaDAO.
 */
public final class SemillaDAO {

	/** The Constant ID_LISTA. */
	private static final String ID_LISTA = "id_lista";
	
	/** The Constant ORDEN. */
	private static final String ORDEN = "orden";
	
	/** The Constant NOMBRE_CAT. */
	private static final String NOMBRE_CAT = "nombreCat";
	
	/** The Constant ID_CATEGORIA. */
	private static final String ID_CATEGORIA = "id_categoria";
	
	/** The Constant LISTA. */
	private static final String LISTA = "lista";
	
	/** The Constant NOMBRE. */
	private static final String NOMBRE = "nombre";
	
	/** The Constant L_ID_LISTA. */
	private static final String L_ID_LISTA = "l.id_lista";
	
	/** The Constant SQL_EXCEPTION. */
	private static final String SQL_EXCEPTION = "SQL Exception: ";

	/**
	 * Instantiates a new semilla DAO.
	 */
	private SemillaDAO() {
	}

	/**
	 * Exist seed.
	 *
	 * @param c             the c
	 * @param nombreSemilla the nombre semilla
	 * @param type          the type
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existSeed(Connection c, String nombreSemilla, int type) throws SQLException {
		final String query;
		if (type == -1) {
			query = "SELECT 1 FROM lista WHERE nombre = ?";
		} else {
			query = "SELECT 1 FROM lista WHERE nombre = ? AND id_tipo_lista = ?";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, nombreSemilla);
			if (type != -1) {
				ps.setLong(2, type);
			}
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Insert list.
	 *
	 * @param c             the c
	 * @param tipoLista     the tipo lista
	 * @param nombreSemilla the nombre semilla
	 * @param listaUrls     the lista urls
	 * @param categoria     the categoria
	 * @param acronimo      the acronimo
	 * @param dependencia   the dependencia
	 * @return the long
	 * @throws Exception the exception
	 */
	public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls,
			String categoria, String acronimo, String dependencia) throws Exception {
		return insertList(c, tipoLista, nombreSemilla, listaUrls, categoria, acronimo, dependencia, true, false);
	}

	/**
	 * Insert list.
	 *
	 * @param c             the c
	 * @param tipoLista     the tipo lista
	 * @param nombreSemilla the nombre semilla
	 * @param listaUrls     the lista urls
	 * @param categoria     the categoria
	 * @param acronimo      the acronimo
	 * @param dependencia   the dependencia
	 * @param activa        the activa
	 * @param inDirectory   the in directory
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls,
			String categoria, String acronimo, String dependencia, boolean activa, boolean inDirectory)
			throws SQLException {
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, dependencia, activa, in_directory) VALUES (?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, tipoLista);
			ps.setString(2, nombreSemilla);
			ps.setString(3, listaUrls);
			if (StringUtils.isNotEmpty(categoria)) {
				ps.setString(4, categoria);
			} else {
				ps.setString(4, null);
			}
			if (StringUtils.isNotEmpty(acronimo)) {
				ps.setString(5, acronimo);
			} else {
				ps.setString(5, null);
			}
			if (StringUtils.isNotEmpty(dependencia)) {
				ps.setString(6, dependencia);
			} else {
				ps.setString(6, null);
			}
			ps.setBoolean(7, activa);
			ps.setBoolean(8, inDirectory);
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return null;
	}

	/**
	 * Gets the seeds.
	 *
	 * @param c    the c
	 * @param type the type
	 * @return the seeds
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeeds(Connection c, int type) throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();

		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM lista l WHERE id_tipo_lista = ? ;")) {
			ps.setLong(1, type);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(L_ID_LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					if (type == Constants.ID_LISTA_SEMILLA_OBSERVATORIO) {
						final CategoriaForm categoriaForm = new CategoriaForm();
						categoriaForm.setId(rs.getString(ID_CATEGORIA));
						categoriaForm.setName(rs.getString(NOMBRE_CAT));
						categoriaForm.setOrden(rs.getInt(ORDEN));
						semillaForm.setCategoria(categoriaForm);
						if (rs.getLong(ID_LISTA) == 0) {
							semillaForm.setAsociada(false);
						} else {
							semillaForm.setAsociada(true);
						}
					}
					seedList.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return seedList;
	}

	/**
	 * Gets the observatory seeds.
	 *
	 * @param c          the c
	 * @param pagina     the pagina
	 * @param searchForm the search form
	 * @return the observatory seeds
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getObservatorySeeds(Connection c, int pagina, SemillaSearchForm searchForm)
			throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;

		int count = 1;

		String query = "SELECT * FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? ";

		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?) ";
		}

		if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
			query += " AND l.id_categoria = ? ";
		}

		if (StringUtils.isNotEmpty(searchForm.getUrl())) {
			query += " AND l.lista like ? ";
		}

		query += " ORDER BY UPPER(l.nombre) ";

		query += " LIMIT ? OFFSET ?";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}

			if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
				ps.setString(count++, searchForm.getCategoria());
			}

			if (StringUtils.isNotEmpty(searchForm.getUrl())) {
				ps.setString(count++, "%" + searchForm.getUrl() + "%");
			}

			ps.setLong(count++, pagSize);
			ps.setLong(count, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(L_ID_LISTA));
					semillaForm.setNombre(rs.getString("l.nombre"));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));

					// Rellenamos campos adicionales para el nuevo grid de
					// búsqueda
					// Multidependencia
					semillaForm.setAcronimo(rs.getString("l.acronimo"));

					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					if (rs.getLong("l.activa") == 0) {
						semillaForm.setActiva(false);
					} else {
						semillaForm.setActiva(true);
					}

					if (rs.getLong("l.in_directory") == 0) {
						semillaForm.setInDirectory(false);
					} else {
						semillaForm.setInDirectory(true);
					}

					if (rs.getLong("l.eliminar") == 0) {
						semillaForm.setEliminar(false);
					} else {
						semillaForm.setEliminar(true);
					}

					
					// Cargar las dependencias de la semilla

					PreparedStatement psDependencias = c.prepareStatement(
							"SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?) ORDER BY UPPER(d.nombre)");
					psDependencias.setLong(1, semillaForm.getId());

					List<DependenciaForm> listDependencias = new ArrayList<>();
					ResultSet rsDependencias = null;

					try {
						rsDependencias = psDependencias.executeQuery();
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString(NOMBRE));
							listDependencias.add(dependencia);
						}

						semillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psDependencias, rsDependencias);
					}

					seedList.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return seedList;
	}

	/**
	 * Count observatory seeds.
	 *
	 * @param c          the c
	 * @param searchForm the search form
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countObservatorySeeds(Connection c, SemillaSearchForm searchForm) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? ";

		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?) ";
		}

		if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
			query += " AND l.id_categoria = ? ";
		}

		if (StringUtils.isNotEmpty(searchForm.getUrl())) {
			query += " AND l.lista like ? ";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}

			if (StringUtils.isNotEmpty(searchForm.getCategoria())) {
				ps.setString(count++, searchForm.getCategoria());
			}

			if (StringUtils.isNotEmpty(searchForm.getUrl())) {
				ps.setString(count, "%" + searchForm.getUrl() + "%");
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the seeds choose.
	 *
	 * @param c          the c
	 * @param pagina     the pagina
	 * @param searchForm the search form
	 * @return the seeds choose
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedsChoose(Connection c, int pagina, SemillaSearchForm searchForm)
			throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;

		int count = 1;
		String query = "SELECT l.*, COUNT(r.id_rastreo) AS rastreos_asociados FROM lista l "
				+ "LEFT JOIN rastreo r ON (l.id_lista = r.semillas) " + "WHERE id_tipo_lista = ? AND id_lista NOT IN ("
				+ "SELECT DISTINCT(dominio) FROM cuenta_cliente) ";

		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND nombre like ? ";
		}

		query += " GROUP BY id_lista LIMIT ? OFFSET ?";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA);

			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}

			ps.setLong(count++, pagSize);
			ps.setLong(count, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					if (rs.getInt("rastreos_asociados") > 0) {
						semillaForm.setAsociada(true);
					} else {
						semillaForm.setAsociada(false);
					}
					seedList.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return seedList;
	}

	/**
	 * Count seeds choose.
	 *
	 * @param c          the c
	 * @param type       the type
	 * @param searchForm the search form
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int countSeedsChoose(Connection c, int type, SemillaSearchForm searchForm) throws SQLException {
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			return countSeedsChooseFilteredByName(c, type, searchForm);
		} else {
			return countSeedsByTipoLista(c, type);
		}
	}

	/**
	 * Count seeds by tipo lista.
	 *
	 * @param c    the c
	 * @param type the type
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	private static int countSeedsByTipoLista(final Connection c, final int type) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN ("
						+ "SELECT DISTINCT(dominio) FROM cuenta_cliente)")) {
			ps.setLong(1, type);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Count seeds choose filtered by name.
	 *
	 * @param c          the c
	 * @param type       the type
	 * @param searchForm the search form
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	private static int countSeedsChooseFilteredByName(Connection c, int type, SemillaSearchForm searchForm)
			throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN ("
						+ "SELECT DISTINCT(dominio) FROM cuenta_cliente)  AND nombre like ?")) {
			ps.setLong(1, type);
			ps.setString(2, "%" + searchForm.getNombre() + "%");

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return 0;
	}

	/**
	 * Convert string to list.
	 *
	 * @param lista the lista
	 * @return the list
	 */
	private static List<String> convertStringToList(final String lista) {
		final List<String> urlsList = new ArrayList<>();
		final StringTokenizer tokenizer = new StringTokenizer(lista, ";");
		while (tokenizer.hasMoreTokens()) {
			urlsList.add(tokenizer.nextToken());
		}
		return urlsList;
	}

	/**
	 * Delete seed.
	 *
	 * @param c         the c
	 * @param idSemilla the id semilla
	 * @throws SQLException the SQL exception
	 */
	public static void deleteSeed(Connection c, long idSemilla) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM lista WHERE lista.id_lista = ?")) {
			ps.setLong(1, idSemilla);
			ps.executeUpdate();

			// Borrar las relacions (no se pueden crear FK a lista por MyISAM no
			// lo permite
			// https://dev.mysql.com/doc/refman/5.7/en/myisam-storage-engine.html
			PreparedStatement deleteSemillaDependencia = c
					.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");

			deleteSemillaDependencia.setLong(1, idSemilla);
			deleteSemillaDependencia.executeUpdate();

		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete observatory seed.
	 *
	 * @param c                   the c
	 * @param idSemilla           the id semilla
	 * @param observatoryFormList the observatory form list
	 * @throws SQLException the SQL exception
	 */
	public static void deleteObservatorySeed(Connection c, long idSemilla, List<ObservatorioForm> observatoryFormList)
			throws SQLException {
		try {
			c.setAutoCommit(false);

			for (ObservatorioForm observatorioForm : observatoryFormList) {
				deleteObservatorySeed(c, idSemilla, observatorioForm.getId());
			}

			try (PreparedStatement ps = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?")) {
				ps.setLong(1, idSemilla);
				ps.executeUpdate();

				c.commit();
			} catch (Exception e) {
				c.rollback();
				c.setAutoCommit(true);
				throw e;
			}
		} catch (SQLException e) {
			c.rollback();
			c.setAutoCommit(true);
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the seed by id.
	 *
	 * @param c         the c
	 * @param idSemilla the id semilla
	 * @return the seed by id
	 * @throws SQLException the SQL exception
	 */
	public static SemillaForm getSeedById(Connection c, long idSemilla) throws SQLException {
		final SemillaForm semillaForm = new SemillaForm();

		try (PreparedStatement ps = c.prepareStatement(
				"SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) WHERE l.id_lista = ?")) {
			ps.setLong(1, idSemilla);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					// Multidependencia

					// Multidependencia

					PreparedStatement psDependencias = c.prepareStatement(
							"SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?) ORDER BY UPPER(d.nombre)");
					psDependencias.setLong(1, semillaForm.getId());

					List<DependenciaForm> listDependencias = new ArrayList<>();
					try (ResultSet rsDependencias = psDependencias.executeQuery()) {
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString(NOMBRE));
							listDependencias.add(dependencia);
						}

						semillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					}

					semillaForm.setAcronimo(rs.getString("acronimo"));
					semillaForm.setActiva(rs.getBoolean("activa"));
					semillaForm.setInDirectory(rs.getBoolean("in_directory"));
					semillaForm.setEliminar(rs.getBoolean("eliminar"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return semillaForm;
	}

	/**
	 * Gets the id list.
	 *
	 * @param c           the c
	 * @param nombreLista the nombre lista
	 * @param idCategoria the id categoria
	 * @return the id list
	 * @throws SQLException the SQL exception
	 */
	public static long getIdList(Connection c, String nombreLista, Long idCategoria) throws SQLException {
		if (idCategoria != null) {
			return getIdListByNombreAndCategoria(c, nombreLista, idCategoria);
		} else {
			return getIdListByNombre(c, nombreLista);
		}
	}

	/**
	 * Gets the id list by nombre.
	 *
	 * @param c           the c
	 * @param nombreLista the nombre lista
	 * @return the id list by nombre
	 * @throws SQLException the SQL exception
	 */
	private static long getIdListByNombre(Connection c, String nombreLista) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT id_lista FROM lista WHERE nombre = ? ORDER BY id_lista DESC LIMIT 1")) {
			ps.setString(1, nombreLista);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(ID_LISTA);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the id list by nombre and categoria.
	 *
	 * @param c           the c
	 * @param nombreLista the nombre lista
	 * @param idCategoria the id categoria
	 * @return the id list by nombre and categoria
	 * @throws SQLException the SQL exception
	 */
	private static long getIdListByNombreAndCategoria(Connection c, String nombreLista, Long idCategoria)
			throws SQLException {
		try (PreparedStatement ps = c.prepareStatement(
				"SELECT id_lista FROM lista WHERE nombre = ? AND id_categoria = ? ORDER BY id_lista DESC LIMIT 1")) {
			ps.setString(1, nombreLista);
			ps.setLong(2, idCategoria);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(ID_LISTA);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Edits the seed.
	 *
	 * @param c           the c
	 * @param semillaForm the semilla form
	 * @throws SQLException the SQL exception
	 */
	public static void editSeed(Connection c, SemillaForm semillaForm) throws SQLException {
		// Multidependencia
		try (PreparedStatement ps = c.prepareStatement(
				"UPDATE lista SET lista = ?, nombre = ?, id_categoria = ?, acronimo = ?, activa = ?, in_directory = ?, eliminar = ? WHERE id_lista = ? ")) {

			ps.setString(1, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
			ps.setString(2, semillaForm.getNombre());
			if (semillaForm.getCategoria().getId() != null
					&& !StringUtils.isEmpty(semillaForm.getCategoria().getId())) {
				ps.setString(3, semillaForm.getCategoria().getId());
			} else {
				ps.setString(3, null);
			}
			if (semillaForm.getAcronimo() != null && !StringUtils.isEmpty(semillaForm.getAcronimo())) {
				ps.setString(4, semillaForm.getAcronimo());
			} else {
				ps.setString(4, null);
			}
			// Multidependencia

			ps.setBoolean(5, semillaForm.isActiva());
			ps.setBoolean(6, semillaForm.isInDirectory());
			ps.setBoolean(7, semillaForm.isEliminar());
			ps.setLong(8, semillaForm.getId());
			ps.executeUpdate();

			// Soporte para múltiples dependencias
			// Borramos las dependencias que pudiera tener antes asociadas
			PreparedStatement psBorradoSemillaDependencia = c
					.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");
			psBorradoSemillaDependencia.setLong(1, semillaForm.getId());
			psBorradoSemillaDependencia.executeUpdate();
			// Inserción de las nuevas

			if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
				StringBuilder slqInsertSemillaDependencia = new StringBuilder(
						"INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");

				for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
					slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",")
							.append(semillaForm.getDependencias().get(i).getId()).append(")");

					if (i < semillaForm.getDependencias().size() - 1) {
						slqInsertSemillaDependencia.append(",");
					}
				}

				PreparedStatement psInsertarSemillaDependencia = c
						.prepareStatement(slqInsertSemillaDependencia.toString());

				psInsertarSemillaDependencia.executeUpdate();
			}

		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Removes the list by id.
	 *
	 * @param c      the c
	 * @param idList the id list
	 * @throws SQLException the SQL exception
	 */
	private static void removeListById(Connection c, long idList) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?")) {
			ps.setLong(1, idList);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Edits the list.
	 *
	 * @param c        the c
	 * @param idLista  the id lista
	 * @param lista    the lista
	 * @param nameList the name list
	 * @throws SQLException the SQL exception
	 */
	private static void editList(Connection c, long idLista, String lista, String nameList) throws SQLException {
		final String query;
		if (nameList.isEmpty()) {
			query = "UPDATE lista SET lista = ? WHERE id_lista = ?";
		} else {
			query = "UPDATE lista SET lista = ?, nombre = ? WHERE id_lista = ?";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, lista);
			if (nameList.isEmpty()) {
				ps.setLong(2, idLista);
			} else {
				ps.setString(2, nameList);
				ps.setLong(3, idLista);
			}

			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Removes the lists.
	 *
	 * @param c                  the c
	 * @param updateListDataForm the update list data form
	 * @throws SQLException the SQL exception
	 */
	public static void removeLists(Connection c, UpdateListDataForm updateListDataForm) throws SQLException {
		if ((updateListDataForm.getListaRastreable() == null || updateListDataForm.getListaRastreable().isEmpty())
				&& updateListDataForm.getIdRastreableAntiguo() != 0) {
			// Si hemos eliminado la lista rastreable, se borra de bbdd
			SemillaDAO.removeListById(c, updateListDataForm.getIdRastreableAntiguo());
		}

		if ((updateListDataForm.getListaNoRastreable() == null || updateListDataForm.getListaNoRastreable().isEmpty())
				&& updateListDataForm.getIdNoRastreableAntiguo() != 0) {
			// Si hemos eliminado la lista no rastreable, se borra de bbdd
			SemillaDAO.removeListById(c, updateListDataForm.getIdNoRastreableAntiguo());
		}
	}

	/**
	 * Update lists.
	 *
	 * @param c                  the c
	 * @param updateListDataForm the update list data form
	 * @return the update list data form
	 * @throws Exception the exception
	 */
	public static UpdateListDataForm updateLists(final Connection c, final UpdateListDataForm updateListDataForm)
			throws Exception {
		// Si no habia lista rastreable y ahora se incluye, se crea
		if (updateListDataForm.getListaRastreable() != null && !updateListDataForm.getListaRastreable().isEmpty()) {
			if (updateListDataForm.getIdListaRastreable() == 0) {
				// Guardamos la lista Rastreable
				insertList(c, Constants.ID_LISTA_RASTREABLE, updateListDataForm.getNombre() + "-Rastreable",
						updateListDataForm.getListaRastreable(), null, null, null);
				final long idCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-Rastreable",
						null);
				updateListDataForm.setIdListaRastreable(idCrawlableList);
			} else {
				editList(c, updateListDataForm.getIdListaRastreable(), updateListDataForm.getListaRastreable(),
						updateListDataForm.getNombre() + "-Rastreable");
			}
		} else {
			if (updateListDataForm.getIdListaRastreable() != 0) {
				updateListDataForm.setIdRastreableAntiguo(updateListDataForm.getIdListaRastreable());
				updateListDataForm.setIdListaRastreable(0);
			}
		}

		// Si no habia lista no rastreable y ahora se incluye, se crea
		if (updateListDataForm.getListaNoRastreable() != null && !updateListDataForm.getListaNoRastreable().isEmpty()) {
			if (updateListDataForm.getIdListaNoRastreable() == 0) {
				// Guardamos la lista no Rastreable
				insertList(c, Constants.ID_LISTA_NO_RASTREABLE, updateListDataForm.getNombre() + "-NoRastreable",
						updateListDataForm.getListaNoRastreable(), null, null, null);
				Long idNoCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-NoRastreable",
						null);
				updateListDataForm.setIdListaNoRastreable(idNoCrawlableList);
			} else {
				editList(c, updateListDataForm.getIdListaNoRastreable(), updateListDataForm.getListaNoRastreable(),
						updateListDataForm.getNombre() + "-NoRastreable");
			}
		} else {
			if (updateListDataForm.getIdListaNoRastreable() != 0) {
				updateListDataForm.setIdNoRastreableAntiguo(updateListDataForm.getIdListaNoRastreable());
				updateListDataForm.setIdListaNoRastreable((long) 0);
			}
		}

		return updateListDataForm;
	}

	/**
	 * Delete observatory seed.
	 *
	 * @param c             the c
	 * @param idSeed        the id seed
	 * @param idObservatory the id observatory
	 * @throws SQLException the SQL exception
	 */
	public static void deleteObservatorySeed(Connection c, long idSeed, long idObservatory) throws SQLException {
		// Se recupera el id del rastreo asociado a la semilla
		try (PreparedStatement ps = c
				.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE id_observatorio = ? AND semillas = ? ")) {
			ps.setLong(2, idSeed);
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					RastreoDAO.borrarRastreo(c, rs.getLong("id_rastreo"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the seed categories.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the seed categories
	 * @throws SQLException the SQL exception
	 */
	public static List<CategoriaForm> getSeedCategories(Connection c, int page) throws SQLException {
		final List<CategoriaForm> categories = new ArrayList<>();
		final String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT * FROM categorias_lista ORDER BY id_categoria ASC";
		} else {
			query = "SELECT * FROM categorias_lista ORDER BY id_categoria ASC LIMIT ? OFFSET ?";
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
					CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString(NOMBRE));
					categoriaForm.setOrden(rs.getInt(ORDEN));
					categories.add(categoriaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return categories;
	}

	/**
	 * Gets the seed category.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @return the seed category
	 * @throws SQLException the SQL exception
	 */
	public static CategoriaForm getSeedCategory(Connection c, long idCategory) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM categorias_lista WHERE id_categoria = ?")) {
			ps.setLong(1, idCategory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString(NOMBRE));
					categoriaForm.setOrden(rs.getInt(ORDEN));
					return categoriaForm;
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the seeds by category.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @param page       the page
	 * @param searchForm the search form
	 * @return the seeds by category
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedsByCategory(Connection c, long idCategory, int page, SemillaForm searchForm)
			throws SQLException {
		final List<SemillaForm> results = new ArrayList<>();
		String query = "SELECT * FROM lista l WHERE id_categoria = ? ";

		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND l.nombre like ? ";
		}

		query += "ORDER BY l.nombre ASC ";

		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;
			ps.setLong(count++, idCategory);

			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
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
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setAcronimo(rs.getString("acronimo"));
					// Multidependencia

					PreparedStatement psDependencias = c.prepareStatement(
							"SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?) ORDER BY UPPER(d.nombre)");
					psDependencias.setLong(1, semillaForm.getId());

					List<DependenciaForm> listDependencias = new ArrayList<>();
					try (ResultSet rsDependencias = psDependencias.executeQuery()) {
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString(NOMBRE));
							listDependencias.add(dependencia);
						}

						semillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					}

					// semillaForm.setDependencia(rs.getString("dependencia"));
					semillaForm.setActiva(rs.getBoolean("activa"));
					semillaForm.setInDirectory(rs.getBoolean("in_directory"));
					semillaForm.setEliminar(rs.getBoolean("eliminar"));
					results.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Count seeds by category.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @param searchForm the search form
	 * @return the integer
	 * @throws SQLException the SQL exception
	 */
	public static Integer countSeedsByCategory(Connection c, long idCategory, SemillaForm searchForm)
			throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) AS numSeeds FROM lista WHERE id_categoria = ? ";

		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND nombre like ? ";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, idCategory);
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count, "%" + searchForm.getNombre() + "%");
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("numSeeds");
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count seed categories.
	 *
	 * @param c the c
	 * @return the integer
	 * @throws Exception the exception
	 */
	public static Integer countSeedCategories(Connection c) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) as numCategories FROM categorias_lista");
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("numCategories");
			}
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Delete seed category.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @throws Exception the exception
	 */
	public static void deleteSeedCategory(Connection c, Long idCategory) throws Exception {
		PreparedStatement ps = null;

		try {
			c.setAutoCommit(false);

			List<Long> observatoryIds = getCategoryObservatoryIds(c, idCategory);
			for (Long observatoryId : observatoryIds) {
				// RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
				List<Long> crawlIdsList = RastreoDAO.getCrawlerCategoryIds(c, observatoryId, idCategory);

				for (Long crawlId : crawlIdsList) {
					RastreoDAO.borrarRastreo(c, crawlId);
				}
			}

			ps = c.prepareStatement("DELETE FROM categorias_lista WHERE id_categoria = ?");
			ps.setLong(1, idCategory);
			ps.executeUpdate();

			c.commit();
		} catch (Exception e) {
			Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			try {
				c.rollback();
			} catch (Exception excep) {
				Logger.putLog("Exception: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Gets the category observatory ids.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @return the category observatory ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getCategoryObservatoryIds(Connection c, Long idCategory) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT id_observatorio FROM observatorio_categoria WHERE id_categoria = ?")) {
			ps.setLong(1, idCategory);
			try (ResultSet rs = ps.executeQuery()) {
				final List<Long> observatoriesIdsList = new ArrayList<>();
				while (rs.next()) {
					observatoriesIdsList.add(rs.getLong(1));
				}

				return observatoriesIdsList;
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete category seed.
	 *
	 * @param c         the c
	 * @param idSemilla the id semilla
	 * @throws Exception the exception
	 */
	public static void deleteCategorySeed(Connection c, String idSemilla) throws Exception {
		try {
			final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			deleteObservatorySeed(c, Long.parseLong(idSemilla), observatoryFormList);
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the seed category.
	 *
	 * @param c             the c
	 * @param categoriaForm the categoria form
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static long createSeedCategory(final Connection c, final CategoriaForm categoriaForm) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO categorias_lista (nombre, orden) VALUES (?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, categoriaForm.getName());
			ps.setInt(2, categoriaForm.getOrden());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Update seed category.
	 *
	 * @param c             the c
	 * @param categoriaForm the categoria form
	 * @throws SQLException the SQL exception
	 */
	public static void updateSeedCategory(Connection c, CategoriaForm categoriaForm) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("UPDATE categorias_lista SET nombre = ?, orden=? WHERE id_categoria = ?")) {
			ps.setString(1, categoriaForm.getName());
			ps.setInt(2, categoriaForm.getOrden());
			ps.setString(3, categoriaForm.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Save seeds category.
	 *
	 * @param c          the c
	 * @param semillas   the semillas
	 * @param idCategory the id category
	 * @throws SQLException the SQL exception
	 */
	public static void saveSeedsCategory(Connection c, List<SemillaForm> semillas, String idCategory)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);

			// Multidependencia
			ps = c.prepareStatement(
					"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			for (SemillaForm semillaForm : semillas) {
				ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				ps.setString(2, semillaForm.getNombre());
				ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
				ps.setString(4, idCategory);
				if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
					ps.setString(5, semillaForm.getAcronimo());
				} else {
					ps.setString(5, null);
				}

				if (StringUtils.isNotEmpty(semillaForm.getActivaStr())
						&& semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(6, false);
				} else {
					ps.setBoolean(6, true);
				}

				if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr())
						&& semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(7, false);
				} else {
					ps.setBoolean(7, true);
				}

				if (StringUtils.isNotEmpty(semillaForm.getEliminarStr())
						&& semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(8, false);
				} else {
					ps.setBoolean(8, true);
				}
				
				int affectedRows = ps.executeUpdate();

				if (affectedRows == 0) {
					// ps.close();
					throw new SQLException("Creating user failed, no rows affected.");
				}

				ResultSet generatedKeys = ps.getGeneratedKeys();

				// Multidependencia
				if (generatedKeys.next()) {
					semillaForm.setId(generatedKeys.getLong(1));

					// Inserción de las nuevas

					if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
						StringBuilder slqInsertSemillaDependencia = new StringBuilder(
								"INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");

						for (int i = 0; i < semillaForm.getDependencias().size(); i++) {

							DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);

							// Si viene informado el nombre de la
							// depenedencia
							// es
							// para que se cree nueva. Si el nombre ya existe,
							// se devuelve el id de la dependencia existente

							if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentDependencia.getName())) {

								PreparedStatement psCreateDependencia = c.prepareStatement(
										"INSERT INTO dependencia(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_dependencia=LAST_INSERT_ID(id_dependencia), nombre = ?",
										Statement.RETURN_GENERATED_KEYS);
								psCreateDependencia.setString(1, currentDependencia.getName());
								psCreateDependencia.setString(2, currentDependencia.getName());

								int affectedRowsD = psCreateDependencia.executeUpdate();

								if (affectedRowsD == 0) {
									throw new SQLException("Creating user failed, no rows affected.");
								}

								ResultSet generatedKeysD = psCreateDependencia.getGeneratedKeys();

								if (generatedKeysD.next()) {
									currentDependencia.setId(generatedKeysD.getLong(1));
								} else {
									throw new SQLException("Creating dependencias failed, no ID obtained.");
								}

							}

							slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",")
									.append(currentDependencia.getId()).append(")");

							if (i < semillaForm.getDependencias().size() - 1) {
								slqInsertSemillaDependencia.append(",");
							}
						}

						PreparedStatement psInsertarSemillaDependencia = c
								.prepareStatement(slqInsertSemillaDependencia.toString());

						psInsertarSemillaDependencia.executeUpdate();
					}

				} else {
					throw new SQLException("Creating dependencias failed, no ID obtained.");
				}

			}

			// ps.executeBatch();

			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Save seed multidependencia.
	 *
	 * @param c           the c
	 * @param semillaForm the semilla form
	 * @throws SQLException the SQL exception
	 */
	public static void saveSeedMultidependencia(Connection c, SemillaForm semillaForm) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);

			// Multidependencia

			ps = c.prepareStatement(
					"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			ps.setString(2, semillaForm.getNombre());
			ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));

			if (semillaForm.getCategoria() != null && StringUtils.isNotEmpty(semillaForm.getCategoria().getId())) {
				ps.setString(4, semillaForm.getCategoria().getId());
			} else {
				ps.setString(4, null);
			}

			if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
				ps.setString(5, semillaForm.getAcronimo());
			} else {
				ps.setString(5, null);
			}

			// Multidependencia
			ps.setBoolean(6, semillaForm.isActiva());

			ps.setBoolean(7, semillaForm.isInDirectory());
			
			ps.setBoolean(8, semillaForm.isEliminar());

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			ResultSet generatedKeys = ps.getGeneratedKeys();

			if (generatedKeys.next()) {
				semillaForm.setId(generatedKeys.getLong(1));

				// Inserción de las nuevas

				if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
					StringBuilder slqInsertSemillaDependencia = new StringBuilder(
							"INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");

					for (int i = 0; i < semillaForm.getDependencias().size(); i++) {

						DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);

						slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",")
								.append(currentDependencia.getId()).append(")");

						if (i < semillaForm.getDependencias().size() - 1) {
							slqInsertSemillaDependencia.append(",");
						}
					}

					PreparedStatement psInsertarSemillaDependencia = c
							.prepareStatement(slqInsertSemillaDependencia.toString());

					psInsertarSemillaDependencia.executeUpdate();
				}

			} else {
				throw new SQLException("Creating dependencias failed, no ID obtained.");
			}

			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Update category seeds.
	 *
	 * @param c        the c
	 * @param semillas the semillas
	 * @throws SQLException the SQL exception
	 */
	public static void updateCategorySeeds(Connection c, List<SemillaForm> semillas) throws SQLException {
		if (!semillas.isEmpty()) {
			try {
				c.setAutoCommit(false);
				try (PreparedStatement ps = c.prepareStatement("UPDATE lista SET nombre = ? WHERE id_lista = ?")) {
					for (SemillaForm semillaForm : semillas) {
						ps.setString(1, semillaForm.getNombre());
						ps.setLong(2, semillaForm.getId());
						ps.addBatch();
					}

					ps.executeBatch();
				}

				c.commit();
				c.setAutoCommit(true);
			} catch (SQLException e) {
				c.rollback();
				c.setAutoCommit(true);
				Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
	}

	/**
	 * Devuelve el listado de {@link DependenciaForm} paginado.
	 *
	 * @param c    {@link Connection}
	 * @param page Página a devolver.
	 * @return Listado de dependencias.
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getSeedDependencias(Connection c, int page) throws SQLException {
		final List<DependenciaForm> dependencias = new ArrayList<>();
		String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT * FROM dependencia";
		} else {
			query = "SELECT * FROM dependencia LIMIT ? OFFSET ?";
		}

		query += " ORDER BY UPPER(nombre) ASC ";

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
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("id_dependencia"));
					dependenciaForm.setName(rs.getString(NOMBRE));
					dependencias.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return dependencias;
	}

	/**
	 * Devuelve las dependencias de una semilla.
	 *
	 * @param c         {@link Connection}
	 * @param idSemilla ID de la semilla
	 * @return Listado de dependencias asociadas a la semilla identificada por el ID
	 *         dado.
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getSeedDependenciasById(Connection c, Long idSemilla) throws SQLException {

		List<DependenciaForm> listDependencias = new ArrayList<>();
		PreparedStatement psDependencias = c.prepareStatement(
				"SELECT id_dependencia, nombre FROM dependencia WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?)");
		psDependencias.setLong(1, idSemilla);

		ResultSet rsDependencias = null;
		try {

			rsDependencias = psDependencias.executeQuery();

			while (rsDependencias.next()) {
				DependenciaForm dependencia = new DependenciaForm();
				dependencia.setId(rsDependencias.getLong("id_dependencia"));
				dependencia.setName(rsDependencias.getString(NOMBRE));
				listDependencias.add(dependencia);
			}

		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(psDependencias, rsDependencias);
		}

		return listDependencias;
	}

	/**
	 * Gets the all observatory seeds.
	 *
	 * @param c the c
	 * @return the all observatory seeds
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getAllObservatorySeeds(Connection c) throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();
		int count = 1;
		String query = "SELECT * FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? order by l.id_categoria, l.nombre";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(L_ID_LISTA));
					semillaForm.setNombre(rs.getString("l.nombre"));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));

					// Rellenamos campos adicionales para el nuevo grid de
					// búsqueda
					// Multidependencia
					semillaForm.setAcronimo(rs.getString("l.acronimo"));

					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					if (rs.getLong("l.activa") == 0) {
						semillaForm.setActiva(false);
					} else {
						semillaForm.setActiva(true);
					}

					if (rs.getLong("l.in_directory") == 0) {
						semillaForm.setInDirectory(false);
					} else {
						semillaForm.setInDirectory(true);
					}
					
					if (rs.getLong("l.eliminar") == 0) {
						semillaForm.setEliminar(false);
					} else {
						semillaForm.setEliminar(true);
					}

					semillaForm.setListaUrlsString(rs.getString(LISTA));

					// Cargar las dependencias de la semilla

					PreparedStatement psDependencias = c.prepareStatement(
							"SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?) ORDER BY UPPER(d.nombre)");
					psDependencias.setLong(1, semillaForm.getId());

					List<DependenciaForm> listDependencias = new ArrayList<>();
					ResultSet rsDependencias = null;

					try {
						rsDependencias = psDependencias.executeQuery();
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString(NOMBRE));
							listDependencias.add(dependencia);
						}

						semillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psDependencias, rsDependencias);
					}

					seedList.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return seedList;
	}

	/**
	 * Save or update seed.
	 *
	 * @param c        the c
	 * @param semillas the semillas
	 * @throws SQLException the SQL exception
	 */
	public static void saveOrUpdateSeed(Connection c, List<SemillaForm> semillas) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);

			for (SemillaForm semillaForm : semillas) {

				if (semillaForm.getId() != null) {

					ps = c.prepareStatement(
							"INSERT INTO lista (id_lista,id_tipo_lista, nombre, lista, id_categoria, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE id_lista=LAST_INSERT_ID(id_lista), id_tipo_lista = ?, nombre = ?, lista = ?, id_categoria = ?, acronimo = ?, activa = ?, in_directory= ?, eliminar=?",
							Statement.RETURN_GENERATED_KEYS);

					ps.setLong(1, semillaForm.getId());
					ps.setInt(2, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(3, semillaForm.getNombre());
					ps.setString(4, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(5, semillaForm.getCategoria().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(6, semillaForm.getAcronimo());
					} else {
						ps.setString(6, null);
					}

					if (StringUtils.isNotEmpty(semillaForm.getActivaStr())
							&& semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(7, false);
					} else {
						ps.setBoolean(7, true);
					}

					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr())
							&& semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(8, false);
					} else {
						ps.setBoolean(8, true);
					}

					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr())
							&& semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(9, false);
					} else {
						ps.setBoolean(9, true);
					}
					
					/*******************/

					ps.setInt(10, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(11, semillaForm.getNombre());
					ps.setString(12, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(13, semillaForm.getCategoria().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(14, semillaForm.getAcronimo());
					} else {
						ps.setString(14, null);
					}
					if (StringUtils.isNotEmpty(semillaForm.getActivaStr())
							&& semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(15, false);
					} else {
						ps.setBoolean(15, true);
					}

					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr())
							&& semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(16, false);
					} else {
						ps.setBoolean(16, true);
					}
					
					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr())
							&& semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(17, false);
					} else {
						ps.setBoolean(17, true);
					}

				} else {

					ps = c.prepareStatement(
							"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);

					ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(2, semillaForm.getNombre());
					ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(4, semillaForm.getCategoria().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(5, semillaForm.getAcronimo());
					} else {
						ps.setString(5, null);
					}
					if (StringUtils.isNotEmpty(semillaForm.getActivaStr())
							&& semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(6, false);
					} else {
						ps.setBoolean(6, true);
					}

					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr())
							&& semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(7, false);
					} else {
						ps.setBoolean(7, true);
					}
					
					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr())
							&& semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(8, false);
					} else {
						ps.setBoolean(8, true);
					}

				}

				int affectedRows = ps.executeUpdate();

				if (affectedRows == 0) {
					// ps.close();
					throw new SQLException("Creating user failed, no rows affected.");
				}

				ResultSet generatedKeys = ps.getGeneratedKeys();

				// Multidependencia
				if (generatedKeys.next()) {
					semillaForm.setId(generatedKeys.getLong(1));

					// Borrar las relaciones (no se pueden crear FK a lista por MyISAM no
					// lo permite
					try {
						PreparedStatement deleteSemillaDependencia = c
								.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");

						deleteSemillaDependencia.setLong(1, semillaForm.getId());
						deleteSemillaDependencia.executeUpdate();

					} catch (SQLException e) {
						Logger.putLog("Error al eliminar las dependencias previas", SemillaDAO.class,
								Logger.LOG_LEVEL_ERROR, e);
					}

					// Inserción de las nuevas

					if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
						StringBuilder slqInsertSemillaDependencia = new StringBuilder(
								"INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");

						for (int i = 0; i < semillaForm.getDependencias().size(); i++) {

							DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);

							// Si viene informado el nombre de la
							// depenedencia
							// es
							// para que se cree nueva. Si el nombre ya existe,
							// se devuelve el id de la dependencia existente

							if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentDependencia.getName())) {

								PreparedStatement psCreateDependencia = c.prepareStatement(
										"INSERT INTO dependencia(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_dependencia=LAST_INSERT_ID(id_dependencia), nombre = ?",
										Statement.RETURN_GENERATED_KEYS);
								psCreateDependencia.setString(1, currentDependencia.getName());
								psCreateDependencia.setString(2, currentDependencia.getName());

								int affectedRowsD = psCreateDependencia.executeUpdate();

								if (affectedRowsD == 0) {
									throw new SQLException("Creating user failed, no rows affected.");
								}

								ResultSet generatedKeysD = psCreateDependencia.getGeneratedKeys();

								if (generatedKeysD.next()) {
									currentDependencia.setId(generatedKeysD.getLong(1));
								} else {
									throw new SQLException("Creating dependencias failed, no ID obtained.");
								}

							}

							slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",")
									.append(currentDependencia.getId()).append(")");

							if (i < semillaForm.getDependencias().size() - 1) {
								slqInsertSemillaDependencia.append(",");
							}
						}

						PreparedStatement psInsertarSemillaDependencia = c
								.prepareStatement(slqInsertSemillaDependencia.toString());

						psInsertarSemillaDependencia.executeUpdate();
					}

				} else {
					throw new SQLException("Creating dependencias failed, no ID obtained.");
				}

			}

			// ps.executeBatch();

			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

}