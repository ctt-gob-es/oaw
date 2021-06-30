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
import es.inteco.rastreador2.actionform.etiquetas.ClasificacionForm;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.actionform.semillas.UpdateListDataForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class SemillaDAO.
 */
public final class SemillaDAO {
	/** The Constant CLAVE. */
	private static final String CLAVE = "clave";
	/** The Constant ID_LISTA. */
	private static final String ID_LISTA = "id_lista";
	/** The Constant ORDEN. */
	private static final String ORDEN = "orden";
	/** The Constant NOMBRE_CAT. */
	private static final String NOMBRE_CAT = "nombreCat";
	/** The Constant ID_CATEGORIA. */
	private static final String ID_CATEGORIA = "id_categoria";
	/** The Constant NOMBRE_AMB. */
	private static final String NOMBRE_AMB = "al.nombre";
	/** The Constant ID_AMBITO. */
	private static final String ID_AMBITO = "id_ambito";
	/** The Constant NOMBRE_COM. */
	private static final String NOMBRE_COM = "cxl.nombre";
	/** The Constant ID_COMPLEJIDAD. */
	private static final String ID_COMPLEJIDAD = "id_complejidad";
	/** The Constant PROFUNDIDAD. */
	private static final String PROFUNDIDAD = "cxl.profundidad";
	/** The Constant AMPLITUD. */
	private static final String AMPLITUD = "cxl.amplitud";
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
	 * Exist seed.
	 *
	 * @param c             the c
	 * @param nombreSemilla the nombre semilla
	 * @param type          the type
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static Long existOtherSeed(Connection c, String nombreSemilla, int type) throws SQLException {
		final String query;
		Long id = null;
		if (type == -1) {
			query = "SELECT id_lista FROM lista WHERE nombre = ?";
		} else {
			query = "SELECT id_lista FROM lista WHERE nombre = ? AND id_tipo_lista = ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, nombreSemilla);
			if (type != -1) {
				ps.setLong(2, type);
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					id = rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			return null;
		}
		return id;
	}

	/**
	 * Insert list.
	 *
	 * @param c             the c
	 * @param tipoLista     the tipo lista
	 * @param nombreSemilla the nombre semilla
	 * @param listaUrls     the lista urls
	 * @param categoria     the categoria
	 * @param ambito        the ambito
	 * @param complejidad   the complejidad
	 * @param etiquetas     the etiquetas
	 * @param acronimo      the acronimo
	 * @param dependencia   the dependencia
	 * @return the long
	 * @throws Exception the exception
	 */
	public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls, String categoria, String ambito, String complejidad, String etiquetas, String acronimo,
			String dependencia) throws Exception {
		return insertList(c, tipoLista, nombreSemilla, listaUrls, categoria, ambito, complejidad, etiquetas, acronimo, dependencia, true, false);
	}

	/**
	 * Insert list.
	 *
	 * @param c             the c
	 * @param tipoLista     the tipo lista
	 * @param nombreSemilla the nombre semilla
	 * @param listaUrls     the lista urls
	 * @param categoria     the categoria
	 * @param ambito        the ambito
	 * @param complejidad   the complejidad
	 * @param etiquetas     the etiquetas
	 * @param acronimo      the acronimo
	 * @param dependencia   the dependencia
	 * @param activa        the activa
	 * @param inDirectory   the in directory
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static Long insertList(Connection c, long tipoLista, String nombreSemilla, String listaUrls, String categoria, String ambito, String complejidad, String etiquetas, String acronimo,
			String dependencia, boolean activa, boolean inDirectory) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, dependencia, activa, in_directory) VALUES (?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, tipoLista);
			ps.setString(2, nombreSemilla);
			ps.setString(3, listaUrls);
			if (StringUtils.isNotEmpty(categoria)) {
				ps.setString(4, categoria);
			} else {
				ps.setString(4, null);
			}
			if (StringUtils.isNotEmpty(ambito)) {
				ps.setString(5, ambito);
			} else {
				ps.setString(5, null);
			}
			if (StringUtils.isNotEmpty(complejidad)) {
				ps.setString(6, complejidad);
			} else {
				ps.setString(6, null);
			}
			if (StringUtils.isNotEmpty(acronimo)) {
				ps.setString(7, acronimo);
			} else {
				ps.setString(7, null);
			}
			if (StringUtils.isNotEmpty(dependencia)) {
				ps.setString(8, dependencia);
			} else {
				ps.setString(8, null);
			}
			ps.setBoolean(9, activa);
			ps.setBoolean(10, inDirectory);
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
		try (PreparedStatement ps = c
				.prepareStatement("SELECT * FROM lista l LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) "
						+ "LEFT JOIN semilla_etiqueta see ON (see.id_lista = l.id_lista) LEFT JOIN etiqueta et ON (et.id_etiqueta = see.id_etiqueta) WHERE id_tipo_lista = ? ;")) {
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
						final AmbitoForm ambitoForm = new AmbitoForm();
						ambitoForm.setId(rs.getString(ID_AMBITO));
						ambitoForm.setName(rs.getString(NOMBRE_AMB));
						semillaForm.setAmbito(ambitoForm);
						final ComplejidadForm complejidadForm = new ComplejidadForm();
						complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
						complejidadForm.setName(rs.getString(NOMBRE_COM));
						complejidadForm.setProfundidad(rs.getInt(PROFUNDIDAD));
						complejidadForm.setAmplitud(rs.getInt(AMPLITUD));
						semillaForm.setComplejidad(complejidadForm);
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
	public static List<SemillaForm> getObservatorySeeds(Connection c, int pagina, SemillaSearchForm searchForm) throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		int count = 1;
		String query = "SELECT distinct l.*, cl.*, cxl.*, al.* FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) LEFT JOIN semilla_dependencia sd ON(l.id_lista = sd.id_lista) LEFT JOIN semilla_etiqueta se ON(l.id_lista = se.id_lista) WHERE id_tipo_lista = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?) ";
		}
		if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getCategoria().length; i++) {
				query = query + " OR l.id_categoria = ?";
			}
			query = query + ")";
		}
		if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getAmbito().length; i++) {
				query = query + " OR l.id_ambito = ?";
			}
			query = query + ")";
		}
		if (StringUtils.isNotEmpty(searchForm.getUrl())) {
			query += " AND l.lista like ? ";
		}
		if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getDependencia().length; i++) {
				query = query + " OR sd.id_dependencia = ?";
			}
			query = query + ")";
		}
		if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getComplejidad().length; i++) {
				query = query + " OR l.id_complejidad = ?";
			}
			query = query + ")";
		}
		if (searchForm.getinDirectorio() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
			query += " AND l.in_directory = ? ";
		}
		if (searchForm.getisActiva() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
			query += " AND l.activa = ? ";
		}
		if (searchForm.getEliminada() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
			query += " AND l.eliminar = ? ";
		}
		if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
				query = query + " OR se.id_etiqueta = ?";
			}
			query = query + ")";
		}
		query += " ORDER BY UPPER(l.nombre) ";
		query += " LIMIT ? OFFSET ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}
			if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
				for (int i = 0; i < searchForm.getCategoria().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getCategoria()[i]));
				}
			}
			if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
				for (int i = 0; i < searchForm.getAmbito().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getAmbito()[i]));
				}
			}
			if (StringUtils.isNotEmpty(searchForm.getUrl())) {
				ps.setString(count++, "%" + searchForm.getUrl() + "%");
			}
			if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
				for (int i = 0; i < searchForm.getDependencia().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getDependencia()[i]));
				}
			}
			if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
				for (int i = 0; i < searchForm.getComplejidad().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getComplejidad()[i]));
				}
			}
			if (searchForm.getinDirectorio() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
				ps.setString(count++, String.valueOf(searchForm.getinDirectorio()));
			}
			if (searchForm.getisActiva() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
				ps.setString(count++, String.valueOf(searchForm.getisActiva()));
			}
			if (searchForm.getEliminada() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
				ps.setString(count++, String.valueOf(searchForm.getEliminada()));
			}
			if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
				for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getEtiquetas()[i]));
				}
			}
			ps.setLong(count++, pagSize);
			ps.setLong(count, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(L_ID_LISTA));
					semillaForm.setNombre(rs.getString("l.nombre"));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setObservaciones(rs.getString("l.observaciones"));
					// Rellenamos campos adicionales para el nuevo grid de
					// búsqueda
					// Multidependencia
					semillaForm.setAcronimo(rs.getString("l.acronimo"));
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE_AMB));
					semillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString(NOMBRE_COM));
					complejidadForm.setProfundidad(rs.getInt(PROFUNDIDAD));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					semillaForm.setComplejidad(complejidadForm);
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
					// Cargar las etiquetas de la semilla
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					ResultSet rsEtiquetas = null;
					try {
						rsEtiquetas = psEtiquetas.executeQuery();
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
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
	 * Gets the observatory seeds to export.
	 *
	 * @param c          the c
	 * @param searchForm the search form
	 * @return the observatory seeds
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getObservatorySeedsToExport(Connection c, SemillaSearchForm searchForm) throws SQLException { // hay que poner los filtros
		final List<SemillaForm> seedList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		int count = 1;
		String query = "SELECT distinct l.*, cl.*, cxl.*, al.* FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) LEFT JOIN semilla_dependencia sd ON(l.id_lista = sd.id_lista) LEFT JOIN semilla_etiqueta se ON(l.id_lista = se.id_lista) WHERE id_tipo_lista = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?) ";
		}
		if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getCategoria().length; i++) {
				query = query + " OR l.id_categoria = ?";
			}
			query = query + ")";
		}
		if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getAmbito().length; i++) {
				query = query + " OR l.id_ambito = ?";
			}
			query = query + ")";
		}
		if (StringUtils.isNotEmpty(searchForm.getUrl())) {
			query += " AND l.lista like ? ";
		}
		if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getDependencia().length; i++) {
				query = query + " OR sd.id_dependencia = ?";
			}
			query = query + ")";
		}
		if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getComplejidad().length; i++) {
				query = query + " OR l.id_complejidad = ?";
			}
			query = query + ")";
		}
		if (StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
			query += " AND l.in_directory = ? ";
		}
		if (StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
			query += " AND l.activa = ? ";
		}
		if (StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
			query += " AND l.eliminar = ? ";
		}
		if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
				query = query + " OR se.id_etiqueta = ?";
			}
			query = query + ")";
		}
		query += " ORDER BY UPPER(l.nombre) ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}
			if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
				for (int i = 0; i < searchForm.getCategoria().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getCategoria()[i]));
				}
			}
			if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
				for (int i = 0; i < searchForm.getAmbito().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getAmbito()[i]));
				}
			}
			if (StringUtils.isNotEmpty(searchForm.getUrl())) {
				ps.setString(count++, "%" + searchForm.getUrl() + "%");
			}
			if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
				for (int i = 0; i < searchForm.getDependencia().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getDependencia()[i]));
				}
			}
			if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
				for (int i = 0; i < searchForm.getComplejidad().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getComplejidad()[i]));
				}
			}
			if (StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
				ps.setString(count++, String.valueOf(searchForm.getinDirectorio()));
			}
			if (StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
				ps.setString(count++, String.valueOf(searchForm.getisActiva()));
			}
			if (StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
				ps.setString(count++, String.valueOf(searchForm.getEliminada()));
			}
			if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
				for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getEtiquetas()[i]));
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(L_ID_LISTA));
					semillaForm.setNombre(rs.getString("l.nombre"));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					// Rellenamos campos adicionales para el nuevo grid de
					// búsqueda
					// Multidependencia
					semillaForm.setAcronimo(rs.getString("l.acronimo"));
					semillaForm.setObservaciones(rs.getString("l.observaciones"));
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE_AMB));
					semillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString(NOMBRE_COM));
					complejidadForm.setProfundidad(rs.getInt(PROFUNDIDAD));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					semillaForm.setComplejidad(complejidadForm);
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
					// Cargar las etiquetas de la semilla
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre, e.id_clasificacion FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					ResultSet rsEtiquetas = null;
					try {
						rsEtiquetas = psEtiquetas.executeQuery();
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							ClasificacionForm cls = new ClasificacionForm();
							cls.setId(rsEtiquetas.getString("id_clasificacion"));
							etiqueta.setClasificacion(cls);
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
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
		// String query = "SELECT COUNT(*) FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) WHERE id_tipo_lista = ? ";
		// String query = "SELECT COUNT(*) FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) LEFT JOIN semilla_dependencia sd ON(l.id_lista = sd.id_lista) LEFT JOIN
		// semilla_etiqueta se ON(l.id_lista = se.id_lista) WHERE id_tipo_lista = ? ";
		String query = "SELECT distinct l.* FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) LEFT JOIN semilla_dependencia sd ON(l.id_lista = sd.id_lista) LEFT JOIN semilla_etiqueta se ON(l.id_lista = se.id_lista) WHERE id_tipo_lista = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?) ";
		}
		if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getCategoria().length; i++) {
				query = query + " OR l.id_categoria = ?";
			}
			query = query + ")";
		}
		// query += " AND l.id_categoria IN ? ";
		if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getAmbito().length; i++) {
				query = query + " OR l.id_ambito = ?";
			}
			query = query + ")";
		}
		if (StringUtils.isNotEmpty(searchForm.getUrl())) {
			query += " AND l.lista like ? ";
		}
		if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getDependencia().length; i++) {
				query = query + " OR sd.id_dependencia = ?";
			}
			query = query + ")";
		}
		if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getComplejidad().length; i++) {
				query = query + " OR l.id_complejidad = ?";
			}
			query = query + ")";
		}
		if (searchForm.getinDirectorio() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
			query += " AND l.in_directory = ? ";
		}
		if (searchForm.getisActiva() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
			query += " AND l.activa = ? ";
		}
		if (searchForm.getEliminada() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
			query += " AND l.eliminar = ? ";
		}
		if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
				query = query + " OR se.id_etiqueta = ?";
			}
			query = query + ")";
		}
		query = "SELECT COUNT(*) FROM ( " + query + ") AS R";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(count++, "%" + searchForm.getNombre() + "%");
			}
			if (searchForm.getCategoria() != null && searchForm.getCategoria().length > 0) {
				// String str = Arrays.toString(searchForm.getCategoria());
				// str = '(' + str.substring(1, str.length()-1) + ')';
				// ps.setString(count++, str);
				for (int i = 0; i < searchForm.getCategoria().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getCategoria()[i]));
				}
			}
			if (searchForm.getAmbito() != null && searchForm.getAmbito().length > 0) {
				for (int i = 0; i < searchForm.getAmbito().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getAmbito()[i]));
				}
			}
			if (StringUtils.isNotEmpty(searchForm.getUrl())) {
				ps.setString(count++, "%" + searchForm.getUrl() + "%");
			}
			if (searchForm.getDependencia() != null && searchForm.getDependencia().length > 0) {
				for (int i = 0; i < searchForm.getDependencia().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getDependencia()[i]));
				}
			}
			if (searchForm.getComplejidad() != null && searchForm.getComplejidad().length > 0) {
				for (int i = 0; i < searchForm.getComplejidad().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getComplejidad()[i]));
				}
			}
			if (searchForm.getinDirectorio() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getinDirectorio()))) {
				String str = String.valueOf(searchForm.getinDirectorio());
				ps.setString(count++, str);
			}
			if (searchForm.getisActiva() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getisActiva()))) {
				String str = String.valueOf(searchForm.getisActiva());
				ps.setString(count++, str);
			}
			if (searchForm.getEliminada() != null && StringUtils.isNotEmpty(String.valueOf(searchForm.getEliminada()))) {
				String str = String.valueOf(searchForm.getEliminada());
				ps.setString(count++, str);
			}
			if (searchForm.getEtiquetas() != null && searchForm.getEtiquetas().length > 0) {
				for (int i = 0; i < searchForm.getEtiquetas().length; i++) {
					ps.setLong(count++, Long.parseLong(searchForm.getEtiquetas()[i]));
				}
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
	public static List<SemillaForm> getSeedsChoose(Connection c, int pagina, SemillaSearchForm searchForm) throws SQLException {
		final List<SemillaForm> seedList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		int count = 1;
		String query = "SELECT l.*, COUNT(r.id_rastreo) AS rastreos_asociados FROM lista l " + "LEFT JOIN rastreo r ON (l.id_lista = r.semillas) " + "WHERE id_tipo_lista = ? AND id_lista NOT IN ("
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
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN (" + "SELECT DISTINCT(dominio) FROM cuenta_cliente)")) {
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
	private static int countSeedsChooseFilteredByName(Connection c, int type, SemillaSearchForm searchForm) throws SQLException {
		try (PreparedStatement ps = c
				.prepareStatement("SELECT COUNT(*) FROM lista WHERE id_tipo_lista = ? AND id_lista NOT IN (" + "SELECT DISTINCT(dominio) FROM cuenta_cliente)  AND nombre like ?")) {
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
			PreparedStatement deleteSemillaDependencia = c.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");
			deleteSemillaDependencia.setLong(1, idSemilla);
			deleteSemillaDependencia.executeUpdate();
			PreparedStatement deleteSemillaEtiqueta = c.prepareStatement("DELETE FROM semilla_etiqueta WHERE id_lista = ?");
			deleteSemillaEtiqueta.setLong(1, idSemilla);
			deleteSemillaEtiqueta.executeUpdate();
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
	public static void deleteObservatorySeed(Connection c, long idSemilla, List<ObservatorioForm> observatoryFormList) throws SQLException {
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
				"SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) WHERE l.id_lista = ?")) {
			ps.setLong(1, idSemilla);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					semillaForm.setObservaciones(rs.getString("l.observaciones"));
					CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE_AMB));
					semillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString(NOMBRE_COM));
					complejidadForm.setProfundidad(rs.getInt("cxl.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					semillaForm.setComplejidad(complejidadForm);
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
					// Etiquetas
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre, e.id_clasificacion FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							ClasificacionForm clasificacion = new ClasificacionForm();
							clasificacion.setId(rsEtiquetas.getString("id_clasificacion"));
							etiqueta.setClasificacion(clasificacion);
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
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
	 * Gets the seed by ids.
	 *
	 * @param c          the c
	 * @param idsSemilla the ids semilla
	 * @return the seed by ids
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedByIds(Connection c, List<Long> idsSemilla) throws SQLException {
		final List<SemillaForm> semillasForm = new ArrayList<>();
		String query = "SELECT * FROM lista l LEFT OUTER JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) WHERE l.id_lista in (";
		String temp = "";
		for (int i = 0; i < idsSemilla.size(); i++) {
			temp += ",?";
		}
		temp = temp.replaceFirst(",", "");
		temp += ")";
		query = query + temp;
		try (PreparedStatement ps = c.prepareStatement(query)) {
			for (int i = 0; i < idsSemilla.size(); i++) {
				ps.setLong(i + 1, idsSemilla.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString(ID_CATEGORIA));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					semillaForm.setCategoria(categoriaForm);
					AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE_AMB));
					semillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString(NOMBRE_COM));
					complejidadForm.setProfundidad(rs.getInt("cxl.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					semillaForm.setComplejidad(complejidadForm);
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
					// Etiquetas
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					}
					semillaForm.setAcronimo(rs.getString("acronimo"));
					semillaForm.setActiva(rs.getBoolean("activa"));
					semillaForm.setInDirectory(rs.getBoolean("in_directory"));
					semillaForm.setEliminar(rs.getBoolean("eliminar"));
					semillasForm.add(semillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return semillasForm;
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
		try (PreparedStatement ps = c.prepareStatement("SELECT id_lista FROM lista WHERE nombre = ? ORDER BY id_lista DESC LIMIT 1")) {
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
	private static long getIdListByNombreAndCategoria(Connection c, String nombreLista, Long idCategoria) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_lista FROM lista WHERE nombre = ? AND id_categoria = ? ORDER BY id_lista DESC LIMIT 1")) {
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
				"UPDATE lista SET lista = ?, nombre = ?, id_categoria = ?, id_ambito = ?, id_complejidad = ?, acronimo = ?, activa = ?, in_directory = ?, eliminar = ?, observaciones = ? WHERE id_lista = ? ")) {
			ps.setString(1, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
			ps.setString(2, semillaForm.getNombre());
			if (semillaForm.getCategoria().getId() != null && !StringUtils.isEmpty(semillaForm.getCategoria().getId())) {
				ps.setString(3, semillaForm.getCategoria().getId());
			} else {
				ps.setString(3, null);
			}
			if (semillaForm.getAmbito().getId() != null && !StringUtils.isEmpty(semillaForm.getAmbito().getId())) {
				ps.setString(4, semillaForm.getAmbito().getId());
			} else {
				ps.setString(4, null);
			}
			if (semillaForm.getComplejidad().getId() != null && !StringUtils.isEmpty(semillaForm.getComplejidad().getId())) {
				ps.setString(5, semillaForm.getComplejidad().getId());
			} else {
				ps.setString(5, null);
			}
			if (semillaForm.getAcronimo() != null && !StringUtils.isEmpty(semillaForm.getAcronimo())) {
				ps.setString(6, semillaForm.getAcronimo());
			} else {
				ps.setString(6, null);
			}
			// Multidependencia
			ps.setBoolean(7, semillaForm.isActiva());
			ps.setBoolean(8, semillaForm.isInDirectory());
			ps.setBoolean(9, semillaForm.isEliminar());
			if (semillaForm.getObservaciones() != null && !StringUtils.isEmpty(semillaForm.getObservaciones())) {
				ps.setString(10, semillaForm.getObservaciones());
			} else {
				ps.setString(10, null);
			}
			// WHERE
			ps.setLong(11, semillaForm.getId());
			ps.executeUpdate();
			// Soporte para múltiples dependencias
			// Borramos las dependencias que pudiera tener antes asociadas
			PreparedStatement psBorradoSemillaDependencia = c.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");
			psBorradoSemillaDependencia.setLong(1, semillaForm.getId());
			psBorradoSemillaDependencia.executeUpdate();
			// Inserción de las nuevas
			if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
				StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
				for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
					slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(semillaForm.getDependencias().get(i).getId()).append(")");
					if (i < semillaForm.getDependencias().size() - 1) {
						slqInsertSemillaDependencia.append(",");
					}
				}
				PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
				psInsertarSemillaDependencia.executeUpdate();
			}
			// Borramos las etiquetas que pudiera tener antes asociadas
			PreparedStatement psBorradoSemillaEtiqueta = c.prepareStatement("DELETE FROM semilla_etiqueta WHERE id_lista = ?");
			psBorradoSemillaEtiqueta.setLong(1, semillaForm.getId());
			psBorradoSemillaEtiqueta.executeUpdate();
			// Inserción de las nuevas
			if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
				StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
				for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
					slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(semillaForm.getEtiquetas().get(i).getId()).append(")");
					if (i < semillaForm.getEtiquetas().size() - 1) {
						slqInsertSemillaEtiqueta.append(",");
					}
				}
				PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
				psInsertarSemillaEtiqueta.executeUpdate();
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
		if ((updateListDataForm.getListaRastreable() == null || updateListDataForm.getListaRastreable().isEmpty()) && updateListDataForm.getIdRastreableAntiguo() != 0) {
			// Si hemos eliminado la lista rastreable, se borra de bbdd
			SemillaDAO.removeListById(c, updateListDataForm.getIdRastreableAntiguo());
		}
		if ((updateListDataForm.getListaNoRastreable() == null || updateListDataForm.getListaNoRastreable().isEmpty()) && updateListDataForm.getIdNoRastreableAntiguo() != 0) {
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
	public static UpdateListDataForm updateLists(final Connection c, final UpdateListDataForm updateListDataForm) throws Exception {
		// Si no habia lista rastreable y ahora se incluye, se crea
		if (updateListDataForm.getListaRastreable() != null && !updateListDataForm.getListaRastreable().isEmpty()) {
			if (updateListDataForm.getIdListaRastreable() == 0) {
				// Guardamos la lista Rastreable
				insertList(c, Constants.ID_LISTA_RASTREABLE, updateListDataForm.getNombre() + "-Rastreable", updateListDataForm.getListaRastreable(), null, null, null, null, null, null);
				final long idCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-Rastreable", null);
				updateListDataForm.setIdListaRastreable(idCrawlableList);
			} else {
				editList(c, updateListDataForm.getIdListaRastreable(), updateListDataForm.getListaRastreable(), updateListDataForm.getNombre() + "-Rastreable");
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
				insertList(c, Constants.ID_LISTA_NO_RASTREABLE, updateListDataForm.getNombre() + "-NoRastreable", updateListDataForm.getListaNoRastreable(), null, null, null, null, null, null);
				Long idNoCrawlableList = SemillaDAO.getIdList(c, updateListDataForm.getNombre() + "-NoRastreable", null);
				updateListDataForm.setIdListaNoRastreable(idNoCrawlableList);
			} else {
				editList(c, updateListDataForm.getIdListaNoRastreable(), updateListDataForm.getListaNoRastreable(), updateListDataForm.getNombre() + "-NoRastreable");
			}
		} else {
			if (updateListDataForm.getIdListaNoRastreable() != 0) {
				updateListDataForm.setIdNoRastreableAntiguo(updateListDataForm.getIdListaNoRastreable());
				updateListDataForm.setIdListaNoRastreable(0);
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
		try (PreparedStatement ps = c.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE id_observatorio = ? AND semillas = ? ")) {
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

	///////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the seed ambitos.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the seed ambits
	 * @throws SQLException the SQL exception
	 */
	public static List<AmbitoForm> getSeedAmbits(Connection c, int page) throws SQLException {
		final List<AmbitoForm> ambits = new ArrayList<>();
		final String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT * FROM ambitos_lista ORDER BY id_ambito ASC";
		} else {
			query = "SELECT * FROM ambitos_lista ORDER BY id_ambito ASC LIMIT ? OFFSET ?";
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
					AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE));
					ambits.add(ambitoForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambits;
	}

	/**
	 * Gets the seed ambit.
	 *
	 * @param c       the c
	 * @param idAmbit the id ambit
	 * @return the seed ambit
	 * @throws SQLException the SQL exception
	 */
	public static AmbitoForm getSeedAmbit(Connection c, long idAmbit) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM ambitos_lista WHERE id_ambito = ?")) {
			ps.setLong(1, idAmbit);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE));
					return ambitoForm;
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the seeds by ambit.
	 *
	 * @param c          the c
	 * @param idAmbit    the id ambit
	 * @param page       the page
	 * @param searchForm the search form
	 * @return the seeds by ambit
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedsByAmbit(Connection c, long idAmbit, int page, SemillaForm searchForm) throws SQLException {
		final List<SemillaForm> results = new ArrayList<>();
		String query = "SELECT * FROM lista l WHERE id_ambito = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND l.nombre like ? ";
		}
		query += "ORDER BY l.nombre ASC ";
		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;
			ps.setLong(count++, idAmbit);
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
					// Etiquetas
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
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
	 * Count seeds by ambit.
	 *
	 * @param c          the c
	 * @param idAmbit    the id ambit
	 * @param searchForm the search form
	 * @return the integer
	 * @throws SQLException the SQL exception
	 */
	public static Integer countSeedsByAmbit(Connection c, long idAmbit, SemillaForm searchForm) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) AS numSeeds FROM lista WHERE id_ambito = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND nombre like ? ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, idAmbit);
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
	 * Count seed ambits.
	 *
	 * @param c the c
	 * @return the integer
	 * @throws Exception the exception
	 */
	public static Integer countSeedAmbits(Connection c) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) as numAmbits FROM ambitos_lista"); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("numAmbits");
			}
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the ambit observatory ids.
	 *
	 * @param c       the c
	 * @param idAmbit the id ambit
	 * @return the ambit observatory ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getAmbitObservatoryIds(Connection c, Long idAmbit) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_ambito WHERE id_ambito = ?")) {
			ps.setLong(1, idAmbit);
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
	 * Delete ambit seed.
	 *
	 * @param c         the c
	 * @param idSemilla the id semilla
	 * @throws Exception the exception
	 */
	public static void deleteAmbitSeed(Connection c, String idSemilla) throws Exception {
		try {
			final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			deleteObservatorySeed(c, Long.parseLong(idSemilla), observatoryFormList);
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the seed ambit.
	 *
	 * @param c          the c
	 * @param ambitoForm the ambito form
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static long createSeedAmbit(final Connection c, final AmbitoForm ambitoForm) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO ambitos_lista nombre VALUES ?", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, ambitoForm.getName());
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
	 * Update seed ambito.
	 *
	 * @param c          the c
	 * @param ambitoForm the ambito form
	 * @throws SQLException the SQL exception
	 */
	public static void updateSeedAmbit(Connection c, AmbitoForm ambitoForm) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("UPDATE ambitos_lista SET nombre = ? WHERE id_ambito = ?")) {
			ps.setString(1, ambitoForm.getName());
			ps.setString(2, ambitoForm.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update seed ambito.
	 *
	 * @param c            the c
	 * @param idSeed       the id seed
	 * @param observations the observations
	 * @throws SQLException the SQL exception
	 */
	public static void updateSeedObservations(Connection c, final Integer idSeed, final String observations) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("UPDATE lista SET observaciones = ? WHERE id_lista = ?")) {
			ps.setString(1, observations);
			ps.setInt(2, idSeed);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Save seeds ambit.
	 *
	 * @param c        the c
	 * @param semillas the semillas
	 * @param idAmbit  the id ambit
	 * @throws SQLException the SQL exception
	 */
	public static void saveSeedsAmbit(Connection c, List<SemillaForm> semillas, String idAmbit) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			// Multidependencia
			ps = c.prepareStatement("INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			for (SemillaForm semillaForm : semillas) {
				ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				ps.setString(2, semillaForm.getNombre());
				ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
				if (semillaForm.getCategoria() != null && StringUtils.isNotEmpty(semillaForm.getCategoria().getId())) {
					ps.setString(4, semillaForm.getCategoria().getId());
				} else {
					ps.setString(4, null);
				}
				ps.setString(5, idAmbit);
				if (semillaForm.getComplejidad() != null && StringUtils.isNotEmpty(semillaForm.getComplejidad().getId())) {
					ps.setString(6, semillaForm.getComplejidad().getId());
				} else {
					ps.setString(6, null);
				}
				if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
					ps.setString(7, semillaForm.getAcronimo());
				} else {
					ps.setString(7, null);
				}
				if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(8, false);
				} else {
					ps.setBoolean(8, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(9, false);
				} else {
					ps.setBoolean(9, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(10, false);
				} else {
					ps.setBoolean(10, true);
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
						StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
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
							slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(currentDependencia.getId()).append(")");
							if (i < semillaForm.getDependencias().size() - 1) {
								slqInsertSemillaDependencia.append(",");
							}
						}
						PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
						psInsertarSemillaDependencia.executeUpdate();
					}
				} else {
					throw new SQLException("Creating dependencias failed, no ID obtained.");
				}
				// Etiquetas
				if (generatedKeys.next()) {
					semillaForm.setId(generatedKeys.getLong(1));
					// Inserción de las nuevas
					if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
						StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
						for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
							EtiquetaForm currentEtiqueta = semillaForm.getEtiquetas().get(i);
							// Si viene informado el nombre de la
							// Etiqueta
							// es
							// para que se cree nueva. Si el nombre ya existe,
							// se devuelve el id de la Etiqueta existente
							if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentEtiqueta.getName())) {
								PreparedStatement psCreateEtiqueta = c.prepareStatement(
										"INSERT INTO etiqueta(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_etiqueta=LAST_INSERT_ID(id_etiqueta), nombre = ?", Statement.RETURN_GENERATED_KEYS);
								psCreateEtiqueta.setString(1, currentEtiqueta.getName());
								psCreateEtiqueta.setString(2, currentEtiqueta.getName());
								int affectedRowsD = psCreateEtiqueta.executeUpdate();
								if (affectedRowsD == 0) {
									throw new SQLException("Creating user failed, no rows affected.");
								}
								ResultSet generatedKeysD = psCreateEtiqueta.getGeneratedKeys();
								if (generatedKeysD.next()) {
									currentEtiqueta.setId(generatedKeysD.getLong(1));
								} else {
									throw new SQLException("Creating etiquetas failed, no ID obtained.");
								}
							}
							slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(currentEtiqueta.getId()).append(")");
							if (i < semillaForm.getEtiquetas().size() - 1) {
								slqInsertSemillaEtiqueta.append(",");
							}
						}
						PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
						psInsertarSemillaEtiqueta.executeUpdate();
					}
				} else {
					throw new SQLException("Creating etiquetas failed, no ID obtained.");
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
	///////////////////////////////////////////////////////////////////////////

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
			query = "SELECT * FROM categorias_lista ORDER BY clave IS NULL, clave ASC,  nombre, orden ASC ";
		} else {
			query = "SELECT * FROM categorias_lista ORDER BY clave IS NULL, clave ASC,  nombre, orden ASC LIMIT ? OFFSET ?";
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
					categoriaForm.setKey(rs.getString(CLAVE));
					categoriaForm.setPrincipal(rs.getBoolean("principal"));
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
					categoriaForm.setKey(rs.getString(CLAVE));
					categoriaForm.setPrincipal(rs.getBoolean("principal"));
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
	public static List<SemillaForm> getSeedsByCategory(Connection c, long idCategory, int page, SemillaForm searchForm) throws SQLException {
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
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
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
	 * Gets the seeds observatory.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @param page       the page
	 * @param tags       the tags
	 * @return the seeds observatory
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedsObservatory(Connection c, long idCategory, int page, String[] tags) throws SQLException {
		final List<SemillaForm> results = new ArrayList<>();
		String query = "SELECT DISTINCT l.* FROM lista l LEFT JOIN semilla_etiqueta el ON l.id_lista=el.id_lista WHERE l.id_categoria=? ";
		if (tags != null && tags.length > 0) {
			query = query + " AND ( 1=0 ";
			for (int i = 0; i < tags.length; i++) {
				if (!org.apache.commons.lang3.StringUtils.isEmpty(tags[i])) {
					query = query + " OR el.id_etiqueta= ?";
				}
			}
			query = query + ") ";
		}
		query += "ORDER BY l.nombre ASC ";
		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;
			ps.setLong(count++, idCategory);
			if (tags != null && tags.length > 0) {
				for (int i = 0; i < tags.length; i++) {
					if (!org.apache.commons.lang3.StringUtils.isEmpty(tags[i])) {
						ps.setLong(count++, Long.parseLong(tags[i]));
					}
				}
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
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
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
	public static Integer countSeedsByCategory(Connection c, long idCategory, SemillaForm searchForm) throws SQLException {
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
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) as numCategories FROM categorias_lista"); ResultSet rs = ps.executeQuery()) {
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
		try (PreparedStatement ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_categoria WHERE id_categoria = ?")) {
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
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO categorias_lista (nombre, orden, clave,principal) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, categoriaForm.getName());
			ps.setInt(2, categoriaForm.getOrden());
			ps.setString(3, !org.apache.commons.lang3.StringUtils.isEmpty(categoriaForm.getKey()) ? categoriaForm.getKey() : null);
			ps.setBoolean(4, categoriaForm.isPrincipal());
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
		try (PreparedStatement ps = c.prepareStatement("UPDATE categorias_lista SET nombre = ?, orden=?, clave = ?, principal = ? WHERE id_categoria = ?")) {
			ps.setString(1, categoriaForm.getName());
			ps.setInt(2, categoriaForm.getOrden());
			ps.setString(3, !org.apache.commons.lang3.StringUtils.isEmpty(categoriaForm.getKey()) ? categoriaForm.getKey() : null);
			ps.setBoolean(4, categoriaForm.isPrincipal());
			ps.setString(5, categoriaForm.getId());
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
	public static void saveSeedsCategory(Connection c, List<SemillaForm> semillas, String idCategory) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			// Multidependencia
			ps = c.prepareStatement("INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			for (SemillaForm semillaForm : semillas) {
				ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				ps.setString(2, semillaForm.getNombre());
				ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
				ps.setString(4, idCategory);
				if (semillaForm.getAmbito() != null && StringUtils.isNotEmpty(semillaForm.getAmbito().getId())) {
					ps.setString(5, semillaForm.getAmbito().getId());
				} else {
					ps.setString(5, null);
				}
				if (semillaForm.getComplejidad() != null && StringUtils.isNotEmpty(semillaForm.getComplejidad().getId())) {
					ps.setString(6, semillaForm.getComplejidad().getId());
				} else {
					ps.setString(6, null);
				}
				if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
					ps.setString(7, semillaForm.getAcronimo());
				} else {
					ps.setString(7, null);
				}
				if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(8, false);
				} else {
					ps.setBoolean(8, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(9, false);
				} else {
					ps.setBoolean(9, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(10, false);
				} else {
					ps.setBoolean(10, true);
				}
				int affectedRows = ps.executeUpdate();
				if (affectedRows == 0) {
					// ps.close();
					throw new SQLException("Creating user failed, no rows affected.");
				}
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					semillaForm.setId(generatedKeys.getLong(1));
				}
				// Multidependencia
				// Inserción de las nuevas
				if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
					StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
					for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
						DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);
						// Si viene informado el nombre de la
						// depenedencia
						// es
						// para que se cree nueva. Si el nombre ya existe,
						// se devuelve el id de la dependencia existente
						if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentDependencia.getName())) {
							PreparedStatement psCreateDependencia = c.prepareStatement(
									"INSERT INTO dependencia(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_dependencia=LAST_INSERT_ID(id_dependencia), nombre = ?", Statement.RETURN_GENERATED_KEYS);
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
						slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(currentDependencia.getId()).append(")");
						if (i < semillaForm.getDependencias().size() - 1) {
							slqInsertSemillaDependencia.append(",");
						}
					}
					PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
					psInsertarSemillaDependencia.executeUpdate();
				}
				// Etiquetas
				// Inserción de las nuevas
				if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
					StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
					for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
						EtiquetaForm currentEtiqueta = semillaForm.getEtiquetas().get(i);
						// Si viene informado el nombre de la
						// Etiqueta
						// es
						// para que se cree nueva. Si el nombre ya existe,
						// se devuelve el id de la Etiqueta existente
						if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentEtiqueta.getName())) {
							PreparedStatement psCreateEtiqueta = c.prepareStatement(
									"INSERT INTO etiqueta(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_etiqueta=LAST_INSERT_ID(id_etiqueta), nombre = ?", Statement.RETURN_GENERATED_KEYS);
							psCreateEtiqueta.setString(1, currentEtiqueta.getName());
							psCreateEtiqueta.setString(2, currentEtiqueta.getName());
							int affectedRowsD = psCreateEtiqueta.executeUpdate();
							if (affectedRowsD == 0) {
								throw new SQLException("Creating user failed, no rows affected.");
							}
							ResultSet generatedKeysD = psCreateEtiqueta.getGeneratedKeys();
							if (generatedKeysD.next()) {
								currentEtiqueta.setId(generatedKeysD.getLong(1));
							} else {
								throw new SQLException("Creating etiquetas failed, no ID obtained.");
							}
						}
						slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(currentEtiqueta.getId()).append(")");
						if (i < semillaForm.getEtiquetas().size() - 1) {
							slqInsertSemillaEtiqueta.append(",");
						}
					}
					PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
					psInsertarSemillaEtiqueta.executeUpdate();
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the seed dependendencies.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the seed dependencias
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getSeedDependencies(Connection c, int page) throws SQLException {
		final List<DependenciaForm> dependencies = new ArrayList<>();
		final String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT id_dependencia,nombre FROM dependencia ORDER BY nombre ASC";
		} else {
			query = "SELECT id_dependencia,nombre FROM dependencia ORDER BY nombre ASC LIMIT ? OFFSET ?";
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
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(Long.parseLong(rs.getString("id_dependencia")));
					dependenciaForm.setName(rs.getString("nombre"));
					dependencies.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return dependencies;
	}

	/**
	 * Gets the seed complejidades.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the seed complejidades
	 * @throws SQLException the SQL exception
	 */
	public static List<ComplejidadForm> getSeedComplexities(Connection c, int page) throws SQLException {
		final List<ComplejidadForm> complexities = new ArrayList<>();
		final String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT id_complejidad,nombre,amplitud,profundidad FROM complejidades_lista ORDER BY id_complejidad ASC";
		} else {
			query = "SELECT id_complejidad,nombre,amplitud,profundidad FROM complejidades_lista ORDER BY id_complejidad ASC LIMIT ? OFFSET ?";
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
					ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString("nombre"));
					complejidadForm.setAmplitud(rs.getInt("profundidad"));
					complejidadForm.setProfundidad(rs.getInt("amplitud"));
					complexities.add(complejidadForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return complexities;
	}

	/**
	 * Gets the seed complexity.
	 *
	 * @param c            the c
	 * @param idComplexity the id complexity
	 * @return the seed complexity
	 * @throws SQLException the SQL exception
	 */
	public static ComplejidadForm getSeedComplexity(Connection c, long idComplexity) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM complejidades_lista WHERE id_complejidad = ?")) {
			ps.setLong(1, idComplexity);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString(NOMBRE_COM));
					complejidadForm.setProfundidad(rs.getInt(PROFUNDIDAD));
					complejidadForm.setAmplitud(rs.getInt(AMPLITUD));
					return complejidadForm;
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the seeds by ambit.
	 *
	 * @param c            the c
	 * @param idComplexity the id complexity
	 * @param page         the page
	 * @param searchForm   the search form
	 * @return the seeds by complexity
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getSeedsByComplexity(Connection c, long idComplexity, int page, SemillaForm searchForm) throws SQLException {
		final List<SemillaForm> results = new ArrayList<>();
		String query = "SELECT * FROM lista l WHERE id_complexity = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND l.nombre like ? ";
		}
		query += "ORDER BY l.nombre ASC ";
		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;
			ps.setLong(count++, idComplexity);
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
					// Etiquetas
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiquetaa, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					try (ResultSet rsEtiquetas = psEtiquetas.executeQuery()) {
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
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
	// select * from lista where id_lista not in (select semillas from rastreo where id_observatorio = 43) or id_lista not in (select id_lista from rastreos_realizados where id_obs_realizado = 227)

	/**
	 * Gets the candidate seeds.
	 *
	 * @param c                       the c
	 * @param idObservatorio          the id observatorio
	 * @param idObservatorioRealizado the id observatorio realizado
	 * @return the candidate seeds
	 * @throws SQLException the SQL exception
	 */
	public static List<SemillaForm> getCandidateSeeds(Connection c, Long idObservatorio, Long idObservatorioRealizado) throws SQLException {
		final List<SemillaForm> results = new ArrayList<>();
//	    Activa = Verdadero
//	    Eliminada = Falso
//	    Ámbito = ámbito del observatorio al que se van a añadir.
//	    Segmento = alguno de los segmentos del observatorio al que se van a añadir.
//	    Recurrencia = alguno de las recurrencias del observatorio al que se van a añadir.
		// String query = "select * from lista where id_lista not in (select semillas from rastreo where id_observatorio = ?) or id_lista not in (select id_lista from rastreos_realizados where
		// id_obs_realizado = ?) and eliminar = 0 and activa = 1";
		String query = "select * from lista where eliminar = 0 and activa = 1 and id_categoria in (select id_categoria from observatorio_categoria where id_observatorio= ?)"
				+ "and (id_ambito = (select o.id_ambito from  observatorio o where o.id_observatorio = ? and o.id_ambito <> 0) OR 1=1)"
				+ "and id_lista in (select id_lista from semilla_etiqueta where id_etiqueta in"
				+ "(select id_etiqueta from etiqueta where id_clasificacion = 3 and find_in_set(id_etiqueta, (select tags from observatorio where id_observatorio = ?))))"
				+ "and (id_lista not in  (select semillas from rastreo where id_observatorio = ? )"
				+ "or id_lista not in (select id_lista from rastreos_realizados where id_obs_realizado = ?) ) order by nombre";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatorio);
			ps.setLong(2, idObservatorio);
			ps.setLong(3, idObservatorio);
			ps.setLong(4, idObservatorio);
			ps.setLong(5, idObservatorioRealizado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong(ID_LISTA));
					semillaForm.setNombre(rs.getString(NOMBRE));
					semillaForm.setListaUrlsString(rs.getString(LISTA));
					semillaForm.setListaUrls(convertStringToList(rs.getString(LISTA)));
					semillaForm.setAcronimo(rs.getString("acronimo"));
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
	 * Count seeds by ambit.
	 *
	 * @param c            the c
	 * @param idComplexity the id complexity
	 * @param searchForm   the search form
	 * @return the integer
	 * @throws SQLException the SQL exception
	 */
	public static Integer countSeedsByComplexity(Connection c, long idComplexity, SemillaForm searchForm) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) AS numSeeds FROM lista WHERE id_complejidad = ? ";
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND nombre like ? ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, idComplexity);
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
	 * Count seed ambits.
	 *
	 * @param c the c
	 * @return the integer
	 * @throws Exception the exception
	 */
	public static Integer countSeedComplexities(Connection c) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) as numComplexities FROM complejidades_lista"); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("numComplexities");
			}
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the complexity observatory ids.
	 *
	 * @param c            the c
	 * @param idComplexity the id complexity
	 * @return the complexity observatory ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getComplexityObservatoryIds(Connection c, Long idComplexity) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_observatorio FROM observatorio_complejidad WHERE id_complejidad = ?")) {
			ps.setLong(1, idComplexity);
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
	 * Delete complexity seed.
	 *
	 * @param c         the c
	 * @param idSemilla the id semilla
	 * @throws Exception the exception
	 */
	public static void deleteComplexitySeed(Connection c, String idSemilla) throws Exception {
		try {
			final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			deleteObservatorySeed(c, Long.parseLong(idSemilla), observatoryFormList);
		} catch (Exception e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the seed complexity.
	 *
	 * @param c               the c
	 * @param complejidadForm the complejidad form
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static long createSeedComplexity(final Connection c, final ComplejidadForm complejidadForm) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO complejidades_lista(nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, complejidadForm.getName());
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
	 * Update seed complejidad.
	 *
	 * @param c               the c
	 * @param complejidadForm the complejidad form
	 * @throws SQLException the SQL exception
	 */
	public static void updateSeedComplejidad(Connection c, ComplejidadForm complejidadForm) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("UPDATE complejidad_lista SET nombre = ?, profundidad = ?, amplitud = ? WHERE id_categoria = ?")) {
			ps.setString(1, complejidadForm.getName());
			ps.setInt(2, complejidadForm.getProfundidad());
			ps.setInt(2, complejidadForm.getAmplitud());
			ps.setString(3, complejidadForm.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Save seeds ambit.
	 *
	 * @param c            the c
	 * @param semillas     the semillas
	 * @param idComplexity the id complexity
	 * @throws SQLException the SQL exception
	 */
	public static void saveSeedsComplexity(Connection c, List<SemillaForm> semillas, String idComplexity) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			// Multidependencia
			ps = c.prepareStatement("INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar) VALUES (?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			for (SemillaForm semillaForm : semillas) {
				ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				ps.setString(2, semillaForm.getNombre());
				ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
				if (semillaForm.getCategoria() != null && StringUtils.isNotEmpty(semillaForm.getCategoria().getId())) {
					ps.setString(4, semillaForm.getCategoria().getId());
				} else {
					ps.setString(4, null);
				}
				if (semillaForm.getAmbito() != null && StringUtils.isNotEmpty(semillaForm.getAmbito().getId())) {
					ps.setString(5, semillaForm.getAmbito().getId());
				} else {
					ps.setString(5, null);
				}
				ps.setString(6, idComplexity);
				if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
					ps.setString(7, semillaForm.getAcronimo());
				} else {
					ps.setString(7, null);
				}
				if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(8, false);
				} else {
					ps.setBoolean(8, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(9, false);
				} else {
					ps.setBoolean(9, true);
				}
				if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
					ps.setBoolean(10, false);
				} else {
					ps.setBoolean(10, true);
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
						StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
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
							slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(currentDependencia.getId()).append(")");
							if (i < semillaForm.getDependencias().size() - 1) {
								slqInsertSemillaDependencia.append(",");
							}
						}
						PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
						psInsertarSemillaDependencia.executeUpdate();
					}
				} else {
					throw new SQLException("Creating dependencias failed, no ID obtained.");
				}
				// Etiquetas
				if (generatedKeys.next()) {
					semillaForm.setId(generatedKeys.getLong(1));
					// Inserción de las nuevas
					if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
						StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
						for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
							EtiquetaForm currentEtiqueta = semillaForm.getEtiquetas().get(i);
							// Si viene informado el nombre de la
							// Etiqueta
							// es
							// para que se cree nueva. Si el nombre ya existe,
							// se devuelve el id de la Etiqueta existente
							if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentEtiqueta.getName())) {
								PreparedStatement psCreateEtiqueta = c.prepareStatement(
										"INSERT INTO etiqueta(nombre) VALUES (?) ON DUPLICATE KEY UPDATE id_etiqueta=LAST_INSERT_ID(id_etiqueta), nombre = ?", Statement.RETURN_GENERATED_KEYS);
								psCreateEtiqueta.setString(1, currentEtiqueta.getName());
								psCreateEtiqueta.setString(2, currentEtiqueta.getName());
								int affectedRowsD = psCreateEtiqueta.executeUpdate();
								if (affectedRowsD == 0) {
									throw new SQLException("Creating user failed, no rows affected.");
								}
								ResultSet generatedKeysD = psCreateEtiqueta.getGeneratedKeys();
								if (generatedKeysD.next()) {
									currentEtiqueta.setId(generatedKeysD.getLong(1));
								} else {
									throw new SQLException("Creating etiquetas failed, no ID obtained.");
								}
							}
							slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(currentEtiqueta.getId()).append(")");
							if (i < semillaForm.getEtiquetas().size() - 1) {
								slqInsertSemillaEtiqueta.append(",");
							}
						}
						PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
						psInsertarSemillaEtiqueta.executeUpdate();
					}
				} else {
					throw new SQLException("Creating etiquetas failed, no ID obtained.");
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

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
					"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar, observaciones) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			ps.setString(2, semillaForm.getNombre());
			ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
			if (semillaForm.getCategoria() != null && StringUtils.isNotEmpty(semillaForm.getCategoria().getId())) {
				ps.setString(4, semillaForm.getCategoria().getId());
			} else {
				ps.setString(4, null);
			}
			if (semillaForm.getAmbito() != null && StringUtils.isNotEmpty(semillaForm.getAmbito().getId())) {
				ps.setString(5, semillaForm.getAmbito().getId());
			} else {
				ps.setString(5, null);
			}
			if (semillaForm.getComplejidad() != null && StringUtils.isNotEmpty(semillaForm.getComplejidad().getId())) {
				ps.setString(6, semillaForm.getComplejidad().getId());
			} else {
				ps.setString(6, null);
			}
			if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
				ps.setString(7, semillaForm.getAcronimo());
			} else {
				ps.setString(7, null);
			}
			// Multidependencia
			ps.setBoolean(8, semillaForm.isActiva());
			ps.setBoolean(9, semillaForm.isInDirectory());
			ps.setBoolean(10, semillaForm.isEliminar());
			if (semillaForm.getObservaciones() != null && !StringUtils.isEmpty(semillaForm.getObservaciones())) {
				ps.setString(11, semillaForm.getObservaciones());
			} else {
				ps.setString(11, null);
			}
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				semillaForm.setId(generatedKeys.getLong(1));
				// Inserción de las nuevas
				if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
					StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
					for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
						DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);
						slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(currentDependencia.getId()).append(")");
						if (i < semillaForm.getDependencias().size() - 1) {
							slqInsertSemillaDependencia.append(",");
						}
					}
					PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
					psInsertarSemillaDependencia.executeUpdate();
				}
				// Inserción de las nuevas etiquetas
				if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
					StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
					for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
						EtiquetaForm currentEtiqueta = semillaForm.getEtiquetas().get(i);
						slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(currentEtiqueta.getId()).append(")");
						if (i < semillaForm.getEtiquetas().size() - 1) {
							slqInsertSemillaEtiqueta.append(",");
						}
					}
					PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
					psInsertarSemillaEtiqueta.executeUpdate();
				}
			} else {
				throw new SQLException("Creating dependencias or tags failed, no ID obtained.");
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
	 * Gets the seed dependencias by ambit and tags.
	 *
	 * @param c       the c
	 * @param idAmbit the id ambit
	 * @param idsTags the ids tags
	 * @return the seed dependencias by ambit and tags
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getSeedDependenciasByAmbitAndTags(Connection c, final String idAmbit, final String[] idsTags) throws SQLException {
		final List<DependenciaForm> dependencias = new ArrayList<>();
		List<EtiquetaForm> tags = null;
		String query = "SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE 1=1";
		if (!org.apache.commons.lang3.StringUtils.isEmpty(idAmbit)) {
			query += " AND d.id_dependencia IN (SELECT da.id_dependencia FROM dependencia_ambito da WHERE  da.id_ambito = ?) ";
		}
		if ("3".equalsIgnoreCase(idAmbit)) {
			if (idsTags != null && idsTags.length > 0) {
				tags = EtiquetaDAO.getByIdsAndClasification(c, idsTags, 2);
				if (tags != null && !tags.isEmpty()) {
					query += " AND d.id_tag IN (";
					for (int i = 0; i < tags.size(); i++) {
						query += "?";
						if (i < tags.size() - 1) {
							query += ",";
						}
					}
					query += ")";
				}
			}
		}
		query += " ORDER BY UPPER(d.nombre) ASC ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int paramIndex = 1;
			if (!StringUtils.isEmpty(idAmbit)) {
				ps.setLong(paramIndex, Long.parseLong(idAmbit));
				paramIndex++;
			}
			if ("3".equalsIgnoreCase(idAmbit)) {
				if (idsTags != null && idsTags.length > 0) {
					if (tags != null && !tags.isEmpty()) {
						for (int i = 0; i < tags.size(); i++) {
							ps.setLong(paramIndex, tags.get(i).getId());
							paramIndex++;
						}
					}
				}
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
	 * @return Listado de dependencias asociadas a la semilla identificada por el ID dado.
	 * @throws SQLException the SQL exception
	 */
	public static List<DependenciaForm> getSeedDependenciasById(Connection c, Long idSemilla) throws SQLException {
		List<DependenciaForm> listDependencias = new ArrayList<>();
		PreparedStatement psDependencias = c
				.prepareStatement("SELECT id_dependencia, nombre FROM dependencia WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?)");
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
	 * Devuelve el listado de {@link EtiquetaForm} paginado.
	 *
	 * @param c    {@link Connection}
	 * @param page Página a devolver.
	 * @return Listado de etiquetas.
	 * @throws SQLException the SQL exception
	 */
	public static List<EtiquetaForm> getSeedEtiquetas(Connection c, int page) throws SQLException {
		final List<EtiquetaForm> etiquetas = new ArrayList<>();
		String query;
		if (page == Constants.NO_PAGINACION) {
			query = "SELECT * FROM etiqueta";
		} else {
			query = "SELECT * FROM etiqueta LIMIT ? OFFSET ?";
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
					EtiquetaForm etiquetaForm = new EtiquetaForm();
					etiquetaForm.setId(rs.getLong("id_etiqueta"));
					etiquetaForm.setName(rs.getString(NOMBRE));
					etiquetas.add(etiquetaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return etiquetas;
	}

	/**
	 * Devuelve las etiquetas de una semilla.
	 *
	 * @param c         {@link Connection}
	 * @param idSemilla ID de la semilla
	 * @return Listado de etiquetas asociadas a la semilla identificada por el ID dado.
	 * @throws SQLException the SQL exception
	 */
	public static List<EtiquetaForm> getSeedEtiquetasById(Connection c, Long idSemilla) throws SQLException {
		List<EtiquetaForm> listEtiquetas = new ArrayList<>();
		PreparedStatement psEtiquetas = c.prepareStatement("SELECT id_etiqueta, nombre FROM etiqueta WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?)");
		psEtiquetas.setLong(1, idSemilla);
		ResultSet rsEtiquetas = null;
		try {
			rsEtiquetas = psEtiquetas.executeQuery();
			while (rsEtiquetas.next()) {
				EtiquetaForm etiqueta = new EtiquetaForm();
				etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
				etiqueta.setName(rsEtiquetas.getString(NOMBRE));
				listEtiquetas.add(etiqueta);
			}
		} catch (SQLException e) {
			Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
		}
		return listEtiquetas;
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
		String query = "SELECT * FROM lista l LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria)  LEFT JOIN ambitos_lista al ON (al.id_ambito = l.id_ambito) LEFT JOIN complejidades_lista cxl ON (cxl.id_complejidad = l.id_complejidad) WHERE id_tipo_lista = ? order by l.id_categoria, l.nombre";
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
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString(ID_AMBITO));
					ambitoForm.setName(rs.getString(NOMBRE_AMB));
					semillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString(ID_COMPLEJIDAD));
					complejidadForm.setName(rs.getString("cxl.nombre"));
					complejidadForm.setProfundidad(rs.getInt("cxl.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					semillaForm.setComplejidad(complejidadForm);
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
					// Cargar las etiquetas de la semilla
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setLong(1, semillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					ResultSet rsEtiquetas = null;
					try {
						rsEtiquetas = psEtiquetas.executeQuery();
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString(NOMBRE));
							listEtiquetas.add(etiqueta);
						}
						semillaForm.setEtiquetas(listEtiquetas);
					} catch (SQLException e) {
						Logger.putLog(SQL_EXCEPTION, SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
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
							"INSERT INTO lista (id_lista,id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar, observaciones) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE id_lista=LAST_INSERT_ID(id_lista), id_tipo_lista = ?, nombre = ?, lista = ?, id_categoria = ?, id_ambito=?, id_complejidad= ?, acronimo = ?, activa = ?, in_directory= ?, eliminar=?, observaciones=?",
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, semillaForm.getId());
					ps.setInt(2, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(3, semillaForm.getNombre());
					ps.setString(4, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(5, semillaForm.getCategoria().getId());
					ps.setString(6, semillaForm.getAmbito().getId());
					ps.setString(7, semillaForm.getComplejidad().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(8, semillaForm.getAcronimo());
					} else {
						ps.setString(8, null);
					}
					if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(9, false);
					} else {
						ps.setBoolean(9, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(10, false);
					} else {
						ps.setBoolean(10, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(11, false);
					} else {
						ps.setBoolean(11, true);
					}
					ps.setString(12, semillaForm.getObservaciones());
					ps.setInt(13, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(14, semillaForm.getNombre());
					ps.setString(15, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(16, semillaForm.getCategoria().getId());
					ps.setString(17, semillaForm.getAmbito().getId());
					ps.setString(18, semillaForm.getComplejidad().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(19, semillaForm.getAcronimo());
					} else {
						ps.setString(19, null);
					}
					if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(20, false);
					} else {
						ps.setBoolean(20, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(21, false);
					} else {
						ps.setBoolean(21, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(22, false);
					} else {
						ps.setBoolean(22, true);
					}
					ps.setString(23, semillaForm.getObservaciones());
				} else {
					ps = c.prepareStatement(
							"INSERT INTO lista (id_tipo_lista, nombre, lista, id_categoria, id_ambito, id_complejidad, acronimo, activa, in_directory, eliminar,observaciones) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
					ps.setString(2, semillaForm.getNombre());
					ps.setString(3, SeedUtils.getSeedUrlsForDatabase(semillaForm.getListaUrls()));
					ps.setString(4, semillaForm.getCategoria().getId());
					ps.setString(5, semillaForm.getAmbito().getId());
					ps.setString(6, semillaForm.getComplejidad().getId());
					if (StringUtils.isNotEmpty(semillaForm.getAcronimo())) {
						ps.setString(7, semillaForm.getAcronimo());
					} else {
						ps.setString(7, null);
					}
					if (StringUtils.isNotEmpty(semillaForm.getActivaStr()) && semillaForm.getActivaStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(8, false);
					} else {
						ps.setBoolean(8, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getInDirectoryStr()) && semillaForm.getInDirectoryStr().equalsIgnoreCase(Boolean.FALSE.toString())) {
						ps.setBoolean(9, false);
					} else {
						ps.setBoolean(9, true);
					}
					if (StringUtils.isNotEmpty(semillaForm.getEliminarStr()) && semillaForm.getEliminarStr().equalsIgnoreCase(Boolean.TRUE.toString())) {
						ps.setBoolean(10, true);
					} else {
						ps.setBoolean(10, false);
					}
					ps.setString(11, semillaForm.getObservaciones());
				}
				int affectedRows = ps.executeUpdate();
				if (affectedRows == 0) {
					// ps.close();
					throw new SQLException("Creating user failed, no rows affected.");
				}
				ResultSet generatedKeys = ps.getGeneratedKeys();
				try {
					// Multidependencia
					if (generatedKeys.next()) {
						semillaForm.setId(generatedKeys.getLong(1));
						// Borrar las relaciones (no se pueden crear FK a lista por MyISAM no
						// lo permite
						try {
							PreparedStatement deleteSemillaDependencia = c.prepareStatement("DELETE FROM semilla_dependencia WHERE id_lista = ?");
							deleteSemillaDependencia.setLong(1, semillaForm.getId());
							deleteSemillaDependencia.executeUpdate();
						} catch (SQLException e) {
							Logger.putLog("Error al eliminar las dependencias previas", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						}
						// Inserción de las nuevas
						if (semillaForm.getDependencias() != null && !semillaForm.getDependencias().isEmpty()) {
							StringBuilder slqInsertSemillaDependencia = new StringBuilder("INSERT INTO semilla_dependencia(id_lista, id_dependencia) VALUES ");
							for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
								DependenciaForm currentDependencia = semillaForm.getDependencias().get(i);
								// Si viene informado el nombre de la
								// dependencia
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
								slqInsertSemillaDependencia.append("(").append(semillaForm.getId()).append(",").append(currentDependencia.getId()).append(")");
								if (i < semillaForm.getDependencias().size() - 1) {
									slqInsertSemillaDependencia.append(",");
								}
							}
							PreparedStatement psInsertarSemillaDependencia = c.prepareStatement(slqInsertSemillaDependencia.toString());
							psInsertarSemillaDependencia.executeUpdate();
						}
						try {
							PreparedStatement deleteSemillaEtiqueta = c.prepareStatement("DELETE FROM semilla_etiqueta WHERE id_lista = ?");
							deleteSemillaEtiqueta.setLong(1, semillaForm.getId());
							deleteSemillaEtiqueta.executeUpdate();
						} catch (SQLException e) {
							Logger.putLog("Error al eliminar las etiquetas previas", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						}
						// Inserción de las nuevas
						if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty()) {
							StringBuilder slqInsertSemillaEtiqueta = new StringBuilder("INSERT INTO semilla_etiqueta(id_lista, id_etiqueta) VALUES ");
							for (int i = 0; i < semillaForm.getEtiquetas().size(); i++) {
								EtiquetaForm currentEtiqueta = semillaForm.getEtiquetas().get(i);
//							psCreateEtiqueta.setLong(3, EtiquetaDAO.getClasificacionByName(c,currentEtiqueta.getName()));
								// Si viene informado el nombre de la
								// Etiqueta
								// es
								// para que se cree nueva. Si el nombre ya existe,
								// se devuelve el id de la Etiqueta existente pero se actualiza la calsificación por si cambia
								if (org.apache.commons.lang3.StringUtils.isNotEmpty(currentEtiqueta.getName())) {
									PreparedStatement psCreateEtiqueta = c.prepareStatement(
											"INSERT INTO etiqueta(nombre,id_clasificacion) VALUES (?, ?) ON DUPLICATE KEY UPDATE id_etiqueta=LAST_INSERT_ID(id_etiqueta), nombre = ?, id_clasificacion = ?",
											Statement.RETURN_GENERATED_KEYS);
									psCreateEtiqueta.setString(1, currentEtiqueta.getName());
									psCreateEtiqueta.setString(2, currentEtiqueta.getClasificacion().getId());
									psCreateEtiqueta.setString(3, currentEtiqueta.getName());
									psCreateEtiqueta.setString(4, currentEtiqueta.getClasificacion().getId());
									int affectedRowsD = psCreateEtiqueta.executeUpdate();
									if (affectedRowsD == 0) {
										throw new SQLException("Creating user failed, no rows affected.");
									}
									ResultSet generatedKeysD = psCreateEtiqueta.getGeneratedKeys();
									if (generatedKeysD.next()) {
										currentEtiqueta.setId(generatedKeysD.getLong(1));
									} else {
										throw new SQLException("Creating etiquetas failed, no ID obtained.");
									}
								}
								slqInsertSemillaEtiqueta.append("(").append(semillaForm.getId()).append(",").append(EtiquetaDAO.getIdByName(c, currentEtiqueta.getName())).append(")");
								if (i < semillaForm.getEtiquetas().size() - 1) {
									slqInsertSemillaEtiqueta.append(",");
								}
							}
							PreparedStatement psInsertarSemillaEtiqueta = c.prepareStatement(slqInsertSemillaEtiqueta.toString());
							psInsertarSemillaEtiqueta.executeUpdate();
						}
					} else {
						throw new SQLException("Creating dependencias or tags failed, no ID obtained.");
					}
				} catch (Exception e) {
					Logger.putLog("Error", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
				}
				// ps.executeBatch();
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
}
