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
package es.inteco.rastreador2.dao.observatorio;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static org.apache.commons.lang3.StringUtils.leftPad;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.dao.EstadoObservatorioDAO;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ExecutedObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ListadoObservatorio;
import es.inteco.rastreador2.actionform.observatorio.ModificarObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.NuevoObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.observatorio.VerObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.ObservatoryTypeForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.form.ExtraConfigurationForm;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class ObservatorioDAO.
 */
public final class ObservatorioDAO extends DataBaseDAO {
	/**
	 * Instantiates a new observatorio DAO.
	 */
	private ObservatorioDAO() {
	}

	/**
	 * Gets the fulfilled observatory.
	 *
	 * @param c                      the c
	 * @param idObservatory          the id observatory
	 * @param idObservatoryExecution the id observatory execution
	 * @return the fulfilled observatory
	 * @throws Exception the SQL exception
	 */
	public static ObservatorioRealizadoForm getFulfilledObservatory(Connection c, long idObservatory, long idObservatoryExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		try (PreparedStatement ps = c.prepareStatement("SELECT oreal.*, c.aplicacion, o.nombre FROM observatorios_realizados oreal " + "LEFT JOIN cartucho c ON (c.id_cartucho = oreal.id_cartucho) "
				+ "JOIN observatorio o ON (oreal.id_observatorio = o.id_observatorio) " + "WHERE oreal.id_observatorio = ? AND oreal.id = ?")) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObservatoryExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					observatorioRealizadoForm.setId(rs.getLong("id"));
					observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
					observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));
					final ObservatorioForm observatorio = new ObservatorioForm();
					observatorio.setEstado(rs.getInt("estado"));
					observatorio.setNombre(rs.getString("nombre"));
					observatorioRealizadoForm.setObservatorio(observatorio);
					final CartuchoForm cartucho = new CartuchoForm();
					cartucho.setId(rs.getLong("id_cartucho"));
					cartucho.setName(rs.getString("aplicacion"));
					observatorioRealizadoForm.setCartucho(cartucho);
				} else {
					Logger.putLog("Error en getFulfilledObservatory !rs.next ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getFulfilledObservatory", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return observatorioRealizadoForm;
	}

	/**
	 * Gets the fulfilled observatories.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param page          the page
	 * @param date          the date
	 * @param exObsIds      the ex obs ids
	 * @return the fulfilled observatories
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date, final String[] exObsIds) throws Exception {
		return getFulfilledObservatories(c, idObservatory, page, date, true, exObsIds);
	}

	/**
	 * Gets the fulfilled observatories.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param page          the page
	 * @param date          the date
	 * @param desc          the desc
	 * @param exObsIds      the ex obs ids
	 * @return the fulfilled observatories
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioRealizadoForm> getFulfilledObservatories(Connection c, long idObservatory, int page, Date date, boolean desc, final String[] exObsIds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ObservatorioRealizadoForm> results = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String order = "DESC";
		if (!desc) {
			order = "ASC";
		}
		PropertiesManager pmgr = new PropertiesManager();
		int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		int resultFrom = pagSize * page;
		DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		try {
			if (date == null) {
				if (page == Constants.NO_PAGINACION) {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? ORDER BY o.fecha " + order);
				} else {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? ORDER BY o.fecha " + order + " LIMIT ? OFFSET ?");
					ps.setInt(2, pagSize);
					ps.setInt(3, resultFrom);
				}
			} else {
				if (page == Constants.NO_PAGINACION) {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND o.fecha <= ? ORDER BY o.fecha " + order);
				} else {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND o.fecha <= ? ORDER BY o.fecha " + order + " LIMIT ? OFFSET ?");
					ps.setInt(3, pagSize);
					ps.setInt(4, resultFrom);
				}
				ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
			}
			ps.setLong(1, idObservatory);
			rs = ps.executeQuery();
			List<String> tmp = null;
			if (exObsIds != null) {
				tmp = Arrays.asList(exObsIds);
			}
			while (rs.next()) {
				if (tmp != null && !tmp.isEmpty()) {
					if (tmp.contains(String.valueOf(rs.getLong("o.id")))) {
						getExecutedObservatoriesFromResult(c, results, rs, df);
					}
				} else {
					getExecutedObservatoriesFromResult(c, results, rs, df);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return results;
	}

	/**
	 * Gets the fulfilled observatories by tag.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param page          the page
	 * @param date          the date
	 * @param desc          the desc
	 * @param exObsIds      the ex obs ids
	 * @param tagId         the tag id
	 * @return the fulfilled observatories by tag
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioRealizadoForm> getFulfilledObservatoriesByTag(Connection c, long idObservatory, int page, Date date, boolean desc, final String[] exObsIds, final String tagId)
			throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ObservatorioRealizadoForm> results = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String order = "DESC";
		if (!desc) {
			order = "ASC";
		}
		PropertiesManager pmgr = new PropertiesManager();
		int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		int resultFrom = pagSize * page;
		DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		try {
			if (date == null) {
				if (page == Constants.NO_PAGINACION) {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND find_in_set(?, o.tags) ORDER BY o.fecha " + order);
				} else {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND find_in_set(?, o.tags) ORDER BY o.fecha " + order + " LIMIT ? OFFSET ?");
					ps.setInt(2, pagSize);
					ps.setInt(3, resultFrom);
				}
			} else {
				if (page == Constants.NO_PAGINACION) {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND find_in_set(?, o.tags) AND o.fecha <= ? ORDER BY o.fecha " + order);
				} else {
					ps = c.prepareStatement("SELECT o.*, c.aplicacion, ob.* FROM observatorios_realizados o " + "JOIN observatorio ob ON (ob.id_observatorio = o.id_observatorio)"
							+ "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho) " + "WHERE o.id_observatorio = ? AND find_in_set(?, o.tags) AND o.fecha <= ? ORDER BY o.fecha " + order
							+ " LIMIT ? OFFSET ?");
					ps.setInt(3, pagSize);
					ps.setInt(4, resultFrom);
				}
				ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
			}
			ps.setLong(1, idObservatory);
			ps.setString(2, tagId);
			rs = ps.executeQuery();
			List<String> tmp = null;
			if (exObsIds != null) {
				tmp = Arrays.asList(exObsIds);
			}
			while (rs.next()) {
				if (tmp != null && !tmp.isEmpty()) {
					if (tmp.contains(String.valueOf(rs.getLong("o.id")))) {
						getExecutedObservatoriesFromResult(c, results, rs, df);
					}
				} else {
					getExecutedObservatoriesFromResult(c, results, rs, df);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return results;
	}

	/**
	 * Gets the executed observatories from result.
	 *
	 * @param c       the c
	 * @param results the results
	 * @param rs      the rs
	 * @param df      the df
	 * @return the executed observatories from result
	 * @throws Exception the SQL exception
	 */
	private static void getExecutedObservatoriesFromResult(Connection c, List<ObservatorioRealizadoForm> results, ResultSet rs, DateFormat df) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
		observatorioRealizadoForm.setId(rs.getLong("id"));
		observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
		observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));
		String etiquetasAux = rs.getString("tags");
		List<String> tagList = new ArrayList<String>();
		if (rs.getString("tags") != null && rs.getString("tags").length() > 0) {
			String statement = "SELECT nombre FROM etiqueta WHERE id_etiqueta = ";
			etiquetasAux = etiquetasAux.replace('[', ' ');
			etiquetasAux = etiquetasAux.replace(']', ' ');
			etiquetasAux = etiquetasAux.replace(",", " OR id_etiqueta = ");
			statement = statement + etiquetasAux;
			try (PreparedStatement ps2 = c.prepareStatement(statement)) {
				try (ResultSet rs2 = ps2.executeQuery()) {
					while (rs2.next()) {
						tagList.add(rs2.getString("nombre"));
					}
				} catch (SQLException e) {
					Logger.putLog("Error ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
					throw e;
				}
			} catch (SQLException e) {
				Logger.putLog("Error ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		observatorioRealizadoForm.setTags(tagList);
		ObservatorioForm observatorio = new ObservatorioForm();
		observatorio.setEstado(rs.getInt("estado"));
		observatorio.setNombre(rs.getString("ob.nombre"));
		observatorioRealizadoForm.setObservatorio(observatorio);
		CartuchoForm cartucho = new CartuchoForm();
		cartucho.setId(rs.getLong("id_cartucho"));
		cartucho.setName(rs.getString("aplicacion"));
		observatorioRealizadoForm.setCartucho(cartucho);
		results.add(observatorioRealizadoForm);
	}

	/**
	 * Gets the all fulfilled observatories.
	 *
	 * @param c the c
	 * @return the all fulfilled observatories
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioRealizadoForm> getAllFulfilledObservatories(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ObservatorioRealizadoForm> results = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		try (Statement s = c.createStatement()) {
			try (ResultSet rs = s.executeQuery("SELECT o.id, o.fecha, o.id_cartucho, c.aplicacion FROM observatorios_realizados o " + "LEFT JOIN cartucho c ON (c.id_cartucho = o.id_cartucho)")) {
				while (rs.next()) {
					final ObservatorioRealizadoForm observatorioRealizadoForm = new ObservatorioRealizadoForm();
					observatorioRealizadoForm.setId(rs.getLong("id"));
					observatorioRealizadoForm.setFecha(rs.getTimestamp("fecha"));
					observatorioRealizadoForm.setFechaStr(df.format(rs.getTimestamp("fecha")));
					final CartuchoForm cartucho = new CartuchoForm();
					cartucho.setId(rs.getLong("id_cartucho"));
					cartucho.setName(rs.getString("aplicacion"));
					observatorioRealizadoForm.setCartucho(cartucho);
					results.add(observatorioRealizadoForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getAllFulfilledObservatories", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Gets the executed observarories.
	 *
	 * @param c the c
	 * @return the executed observarories
	 * @throws Exception the SQL exception
	 */
	public static List<ExecutedObservatorioForm> getExecutedObservarories(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ExecutedObservatorioForm> results = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		try (Statement s = c.createStatement()) {
			try (ResultSet rs = s.executeQuery(
					"SELECT o.id_observatorio, o.nombre, ore.id, ore.fecha, c.aplicacion, (SELECT al.nombre FROM ambitos_lista al WHERE al.id_ambito=o.id_ambito) as 'ambito', (SELECT ot.name FROM observatorio_tipo ot WHERE ot.id_tipo=o.id_tipo) AS 'tipo' FROM observatorio o JOIN observatorios_realizados ore ON (o.id_observatorio=ore.id_observatorio) \n"
							+ "	LEFT JOIN cartucho c ON (c.id_cartucho = ore.id_cartucho) WHERE c.id_cartucho = 9 ORDER BY ore.fecha DESC")) {
				while (rs.next()) {
					final ExecutedObservatorioForm tmp = new ExecutedObservatorioForm();
					tmp.setIdObservatorio(rs.getInt("o.id_observatorio"));
					tmp.setIdObsEx(rs.getInt("ore.id"));
					tmp.setNombre(rs.getString("o.nombre"));
					tmp.setFechaExSting((df.format(rs.getTimestamp("ore.fecha"))));
					tmp.setFechaEx(rs.getTimestamp("ore.fecha"));
					tmp.setCartucho(rs.getString("c.aplicacion"));
					tmp.setAmbito(rs.getString("ambito"));
					tmp.setTipo(rs.getString("tipo"));
					results.add(tmp);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getAllFulfilledObservatories", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Count fulfilled observatories.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the int
	 * @throws Exception the SQL exception
	 */
	public static int countFulfilledObservatories(Connection c, long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(id) FROM observatorios_realizados WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the observatory list.
	 *
	 * @param c the c
	 * @return the observatory list
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioForm> getObservatoryList(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ObservatorioForm> results = new ArrayList<>();
		try (Statement s = c.createStatement()) {
			try (ResultSet rs = s.executeQuery("SELECT o.id_observatorio, o.nombre, o.fecha_inicio, o.id_cartucho, p.dias, p.cronExpression " + "FROM observatorio o "
					+ "JOIN periodicidad p ON (o.id_periodicidad = p.id_periodicidad) " + "WHERE o.activo = true;")) {
				while (rs.next()) {
					ObservatorioForm observatorioForm = new ObservatorioForm();
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setNombre(rs.getString("nombre"));
					observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
					PeriodicidadForm periodicidadForm = new PeriodicidadForm();
					periodicidadForm.setCronExpression(rs.getString("cronExpression"));
					periodicidadForm.setDias(rs.getInt("dias"));
					observatorioForm.setPeriodicidadForm(periodicidadForm);
					CartuchoForm cartuchoForm = new CartuchoForm();
					cartuchoForm.setId(rs.getLong("id_cartucho"));
					observatorioForm.setCartucho(cartuchoForm);
					results.add(observatorioForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getObservatoryList", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Gets the observatories from seed.
	 *
	 * @param c      the c
	 * @param idSeed the id seed
	 * @return the observatories from seed
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioForm> getObservatoriesFromSeed(Connection c, final String idSeed) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ObservatorioForm> observatoryFormList = new ArrayList<>();
		try (PreparedStatement ps = c
				.prepareStatement("SELECT o.id_observatorio, o.nombre FROM observatorio o " + "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " + "WHERE r.semillas = ?")) {
			ps.setLong(1, Long.parseLong(idSeed));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ObservatorioForm observatorioForm = new ObservatorioForm();
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setNombre(rs.getString("nombre"));
					observatoryFormList.add(observatorioForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return observatoryFormList;
	}

	/**
	 * Gets the category by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the category by id
	 * @throws Exception the SQL exception
	 */
	public static CategoriaForm getCategoryById(Connection c, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final CategoriaForm category = new CategoriaForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM categorias_lista WHERE id_categoria = ? ORDER BY id_categoria ASC")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					category.setId(String.valueOf(rs.getLong("id_categoria")));
					category.setName(rs.getString("nombre"));
					category.setOrden(rs.getInt("orden"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return category;
	}

	/**
	 * Gets the ambit by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the ambit by id
	 * @throws Exception the SQL exception
	 */
	public static AmbitoForm getAmbitById(Connection c, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final AmbitoForm ambit = new AmbitoForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM ambitos_lista WHERE id_ambito = ? ORDER BY id_ambito ASC")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ambit.setId(String.valueOf(rs.getLong("id_ambito")));
					ambit.setName(rs.getString("nombre"));
					ambit.setDescripcion(rs.getString("descripcion"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambit;
	}

	/**
	 * Gets the ambit by id.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the ambit by id
	 * @throws Exception the SQL exception
	 */
	public static AmbitoForm getAmbitByObservatoryId(Connection c, final Long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		AmbitoForm ambit = null;
		try (PreparedStatement ps = c.prepareStatement("SELECT al.* FROM ambitos_lista al JOIN observatorio o ON o.id_ambito= al.id_ambito WHERE o.id_observatorio = ? ORDER BY id_ambito ASC")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ambit = new AmbitoForm();
					ambit.setId(String.valueOf(rs.getLong("id_ambito")));
					ambit.setName(rs.getString("nombre"));
					ambit.setDescripcion(rs.getString("descripcion"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambit;
	}

	/**
	 * Gets the ambit by observatory ex id.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the ambit by observatory ex id
	 * @throws Exception the SQL exception
	 */
	public static AmbitoForm getAmbitByObservatoryExId(Connection c, final Long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		AmbitoForm ambit = null;
		try (PreparedStatement ps = c.prepareStatement(
				"SELECT al.* FROM ambitos_lista al JOIN observatorio o ON o.id_ambito= al.id_ambito JOIN observatorios_realizados ore ON o.id_observatorio=ore.id_observatorio WHERE ore.id = ? ORDER BY id_ambito ASC")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ambit = new AmbitoForm();
					ambit.setId(String.valueOf(rs.getLong("id_ambito")));
					ambit.setName(rs.getString("nombre"));
					ambit.setDescripcion(rs.getString("descripcion"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambit;
	}

	/**
	 * Gets the complexity by id.
	 *
	 * @param c  the c
	 * @param id the id
	 * @return the complexity by id
	 * @throws Exception the SQL exception
	 */
	public static ComplejidadForm getComplexityById(Connection c, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final ComplejidadForm complexity = new ComplejidadForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM complejidades_lista WHERE id_complejidad = ? ORDER BY id_complejidad ASC")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					complexity.setId(String.valueOf(rs.getLong("id_complejidad")));
					complexity.setName(rs.getString("nombre"));
					complexity.setProfundidad(rs.getInt("profundidad"));
					complexity.setAmplitud(rs.getInt("amplitud"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return complexity;
	}

	/**
	 * Gets the observatories from category.
	 *
	 * @param c           the c
	 * @param idCategoria the id categoria
	 * @return the observatories from category
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioForm> getObservatoriesFromCategory(Connection c, String idCategoria) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ObservatorioForm> observatoryFormList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT(o.id_observatorio), o.nombre, o.id_language, o.profundidad, o.amplitud, o.id_cartucho FROM observatorio o "
				+ "JOIN rastreo r ON (o.id_observatorio = r.id_observatorio) " + "JOIN observatorio_categoria oc ON (o.id_observatorio = oc.id_observatorio) "
				+ "WHERE oc.id_categoria = ? OR r.id_rastreo IN (SELECT " + "id_rastreo FROM rastreo WHERE semillas IN (SELECT " + "id_lista FROM lista WHERE id_categoria = ?))")) {
			ps.setLong(1, Long.parseLong(idCategoria));
			ps.setLong(2, Long.parseLong(idCategoria));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ObservatorioForm observatorioForm = new ObservatorioForm();
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setNombre(rs.getString("nombre"));
					observatorioForm.setLenguaje(rs.getLong("id_language"));
					observatorioForm.setProfundidad(rs.getInt("profundidad"));
					observatorioForm.setAmplitud(rs.getInt("amplitud"));
					CartuchoForm cartuchoForm = new CartuchoForm();
					cartuchoForm.setId(rs.getLong("id_cartucho"));
					observatorioForm.setCartucho(cartuchoForm);
					List<CategoriaForm> selectedCategories = getObservatoryCategories(c, observatorioForm.getId());
					observatorioForm.setCategoria(new String[selectedCategories.size()]);
					int i = 0;
					for (CategoriaForm categoria : selectedCategories) {
						observatorioForm.getCategoria()[i] = categoria.getId();
						i++;
					}
					observatoryFormList.add(observatorioForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return observatoryFormList;
	}

	/**
	 * User list.
	 *
	 * @param c                      the c
	 * @param cargarObservatorioForm the cargar observatorio form
	 * @return the cargar observatorio form
	 * @throws Exception the SQL exception
	 */
	public static CargarObservatorioForm userList(Connection c, CargarObservatorioForm cargarObservatorioForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ListadoObservatorio> observatoryUserList = new ArrayList<>();
		try (Statement s = c.createStatement()) {
			try (ResultSet rs = s.executeQuery("SELECT nombre, id_observatorio FROM observatorio o")) {
				int numObs = 0;
				while (rs.next()) {
					numObs++;
					ListadoObservatorio ls = new ListadoObservatorio();
					ls.setNombreObservatorio(rs.getString("nombre"));
					ls.setId_observatorio(rs.getLong("id_observatorio"));
					observatoryUserList.add(ls);
				}
				cargarObservatorioForm.setListadoObservatorio(observatoryUserList);
				cargarObservatorioForm.setNumObservatorios(numObs);
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return cargarObservatorioForm;
	}

	/**
	 * Gets the observatory seeds.
	 *
	 * @param c the c
	 * @return the observatory seeds
	 * @throws Exception the SQL exception
	 */
	public static List<SemillaForm> getObservatorySeeds(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<SemillaForm> seedList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT id_lista, nombre FROM lista WHERE id_tipo_lista = ?")) {
			ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong("id_lista"));
					semillaForm.setNombre(rs.getString("nombre"));
					seedList.add(semillaForm);
				}
				return seedList;
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Observatory list.
	 *
	 * @param c    the c
	 * @param page the page
	 * @return the cargar observatorio form
	 * @throws Exception the SQL exception
	 */
	public static CargarObservatorioForm observatoryList(Connection c, int page) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
		final List<ListadoObservatorio> observatoryList = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * page;
		try (PreparedStatement ps = c
				.prepareStatement("SELECT DISTINCT(o.nombre), o.id_observatorio, c.id_cartucho, c.aplicacion, ot.name, al.nombre as ambito,  o.tags as etiquetas, o.activo as activo"
						+ " FROM observatorio o JOIN cartucho c ON (o.id_cartucho = c.id_cartucho) JOIN observatorio_tipo ot ON (o.id_tipo=ot.id_tipo) LEFT JOIN ambitos_lista al ON al.id_ambito=o.id_ambito "
						+ " ORDER BY o.id_observatorio LIMIT ? OFFSET ?")) {
			ps.setInt(1, pagSize);
			ps.setInt(2, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ListadoObservatorio ls = new ListadoObservatorio();
					ls.setNombreObservatorio(rs.getString("nombre"));
					ls.setId_observatorio(rs.getLong("id_observatorio"));
					ls.setId_cartucho(rs.getLong("id_cartucho"));
					ls.setCartucho(rs.getString("aplicacion"));
					ls.setTipo(rs.getString("name"));
					ls.setAmbito(rs.getString("ambito"));
					String etiquetasAux = rs.getString("etiquetas");
					List<String> tagList = new ArrayList<String>();
					if (rs.getString("etiquetas") != null && rs.getString("etiquetas").length() > 0) {
						String statement = "SELECT nombre FROM etiqueta WHERE id_etiqueta = ";
						etiquetasAux = etiquetasAux.replace('[', ' ');
						etiquetasAux = etiquetasAux.replace(']', ' ');
						etiquetasAux = etiquetasAux.replace(",", " OR id_etiqueta = ");
						statement = statement + etiquetasAux;
						try (PreparedStatement ps2 = c.prepareStatement(statement)) {
							try (ResultSet rs2 = ps2.executeQuery()) {
								while (rs2.next()) {
									tagList.add(rs2.getString("nombre"));
								}
							} catch (SQLException e) {
								Logger.putLog("Error ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
								throw e;
							}
						} catch (SQLException e) {
							Logger.putLog("Error ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
							throw e;
						}
					}
					ls.setEtiquetas(tagList);
					/*
					 * String etiquetaString = ""; for(int i=0; i<tagArr.length; i++) { try (PreparedStatement psAux = c.prepareStatement("SELECT nombre FROM etiqueta WHERE id_etiqueta = ?")){
					 * psAux.setInt(1, Integer.parseInt(tagArr[i])); try (ResultSet rsAux = psAux.executeQuery()) { while (rsAux.next()) { //etiquetaString = etiquetaString +
					 * "<div class='tagbox-token'><span>" + rsAux.getString("nombre") + "</span></div>"; etiquetaString = etiquetaString + rsAux.getString("nombre") + ","; } } }catch (SQLException e)
					 * { Logger.putLog("Error", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e); throw e; } } ls.setEtiquetas(etiquetaString);
					 */
					ls.setEstado(rs.getBoolean("activo"));
					observatoryList.add(ls);
				}
				cargarObservatorioForm.setListadoObservatorio(observatoryList);
				cargarObservatorioForm.setNumObservatorios(observatoryList.size());
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return cargarObservatorioForm;
	}

	/**
	 * Count observatories.
	 *
	 * @param c the c
	 * @return the int
	 * @throws Exception the SQL exception
	 */
	public static int countObservatories(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(id_observatorio) FROM observatorio"); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count seeds.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @return the int
	 * @throws Exception the SQL exception
	 */
	public static int countSeeds(Connection c, long idObservatorio) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM observatorio_lista WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Detele observatory.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @throws Exception the SQL exception
	 */
	public static void deteleObservatory(Connection c, final long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final boolean isAutoCommit = c.getAutoCommit();
		try {
			c.setAutoCommit(false);
			// RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
			final List<Long> idsRastreos = getCrawlerIds(c, idObservatory);
			// BORRAMOS RASTREOS Y RESULTADOS
			for (Long idRastreo : idsRastreos) {
				RastreoDAO.borrarRastreo(c, idRastreo);
			}
			// ELIMINAMOS LAS RELACIONES OBSERVATORIO_SEMILLA
			try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?")) {
				ps.setLong(1, idObservatory);
				ps.executeUpdate();
				DAOUtils.closeQueries(ps, null);
			}
			// ELIMINAMOS EL OBSERVATORIO
			try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio WHERE id_observatorio = ?")) {
				ps.setLong(1, idObservatory);
				ps.executeUpdate();
			}
			c.commit();
			c.setAutoCommit(isAutoCommit);
		} catch (SQLException e) {
			c.rollback();
			c.setAutoCommit(isAutoCommit);
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Detele fulfilled observatory.
	 *
	 * @param connR       the conn R
	 * @param idExecution the id execution
	 * @throws Exception the SQL exception
	 */
	public static void deteleFulfilledObservatory(Connection connR, long idExecution) throws Exception {
		connR = reOpenConnectionIfIsNecessary(connR);
		PreparedStatement ps = null;
		try {
			connR.setAutoCommit(false);
			// RECUPERAMOS LOS IDS DE LOS RASTREOS DEL OBSERVATORIO
			List<Long> crawlingExecutionsIds = getFulfilledCrawlingIds(connR, idExecution);
			RastreoDAO.deleteAnalyse(connR, crawlingExecutionsIds);
			// ELIMINAMOS EL OBSERVATORIO
			ps = connR.prepareStatement("DELETE FROM observatorios_realizados WHERE id = ?");
			ps.setLong(1, idExecution);
			ps.executeUpdate();
			connR.commit();
		} catch (SQLException e) {
			try {
				connR.rollback();
			} catch (SQLException e1) {
				Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Gets the crawler ids.
	 *
	 * @param connR          the conn R
	 * @param idObservatorio the id observatorio
	 * @return the crawler ids
	 * @throws Exception the SQL exception
	 */
	private static List<Long> getCrawlerIds(Connection connR, long idObservatorio) throws Exception {
		connR = reOpenConnectionIfIsNecessary(connR);
		final List<Long> crawlerIds = new ArrayList<>();
		// RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
		try (PreparedStatement ps = connR.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("id_rastreo"));
				}
				return crawlerIds;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the fulfilled crawling ids.
	 *
	 * @param connR       the conn R
	 * @param idExecution the id execution
	 * @return the fulfilled crawling ids
	 * @throws Exception the SQL exception
	 */
	private static List<Long> getFulfilledCrawlingIds(Connection connR, long idExecution) throws Exception {
		connR = reOpenConnectionIfIsNecessary(connR);
		final List<Long> crawlerIds = new ArrayList<>();
		try (PreparedStatement ps = connR.prepareStatement("SELECT id FROM rastreos_realizados rr WHERE id_obs_realizado = ?")) {
			ps.setLong(1, idExecution);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("id"));
				}
				return crawlerIds;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the fulfilled crawling by observatory execution.
	 *
	 * @param connR       the conn R
	 * @param idExecution the id execution
	 * @return the fulfilled crawling by observatory execution
	 * @throws Exception the SQL exception
	 */
	public static List<FulFilledCrawling> getFulfilledCrawlingByObservatoryExecution(Connection connR, long idExecution) throws Exception {
		connR = reOpenConnectionIfIsNecessary(connR);
		final List<FulFilledCrawling> crawlings = new ArrayList<>();
		try (PreparedStatement ps = connR.prepareStatement(
				"SELECT * FROM rastreos_realizados rr JOIN lista l ON (l.id_lista = rr.id_lista) " + "JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) WHERE id_obs_realizado = ?")) {
			ps.setLong(1, idExecution);
			// RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
					fulfilledCrawling.setId(rs.getLong("id"));
					fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
					fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
					fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
					crawlings.add(fulfilledCrawling);
					SemillaForm seed = new SemillaForm();
					seed.setId(rs.getLong("l.id_lista"));
					seed.setNombre(rs.getString("l.nombre"));
					CategoriaForm category = new CategoriaForm();
					category.setId(String.valueOf(rs.getLong("cl.id_categoria")));
					category.setName(rs.getString("cl.nombre"));
					category.setOrden(rs.getInt("cl.orden"));
					seed.setCategoria(category);
					fulfilledCrawling.setSeed(seed);
				}
				return crawlings;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Insert observatory.
	 *
	 * @param c                     the c
	 * @param nuevoObservatorioForm the nuevo observatorio form
	 * @return the long
	 * @throws Exception the exception
	 */
	public static long insertObservatory(Connection c, NuevoObservatorioForm nuevoObservatorioForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement(
					"INSERT INTO observatorio (nombre, id_periodicidad, profundidad, amplitud, fecha_inicio, id_guideline, pseudoaleatorio, id_language, id_cartucho, activo, id_tipo, id_ambito,tags) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?, ?, ?)");
			ps.setString(1, nuevoObservatorioForm.getNombre());
			ps.setLong(2, Long.parseLong(nuevoObservatorioForm.getPeriodicidad()));
			ps.setInt(3, Integer.parseInt(nuevoObservatorioForm.getProfundidad()));
			ps.setInt(4, Integer.parseInt(nuevoObservatorioForm.getAmplitud()));
			ps.setTimestamp(5, new Timestamp(nuevoObservatorioForm.getFecha().getTime()));
			ps.setLong(6, CartuchoDAO.getGuideline(c, nuevoObservatorioForm.getCartucho().getId()));
			ps.setBoolean(7, Boolean.valueOf(nuevoObservatorioForm.getPseudoAleatorio()));
			ps.setLong(8, nuevoObservatorioForm.getLenguaje());
			ps.setLong(9, nuevoObservatorioForm.getCartucho().getId());
			ps.setBoolean(10, nuevoObservatorioForm.isActivo());
			ps.setLong(11, nuevoObservatorioForm.getTipo().getId());
			ps.setString(12, nuevoObservatorioForm.getAmbitoForm().getId());
			ps.setString(13, nuevoObservatorioForm.getTagsString());
			ps.executeUpdate();
			long idObservatory = 0;
			ps = c.prepareStatement("SELECT id_observatorio FROM observatorio WHERE Nombre = ?");
			ps.setString(1, nuevoObservatorioForm.getNombre());
			rs = ps.executeQuery();
			if (rs.next()) {
				idObservatory = rs.getLong("id_observatorio");
			}
			ps = c.prepareStatement("INSERT INTO observatorio_categoria VALUES (?,?)");
			if (nuevoObservatorioForm.getCategoria() != null) {
				for (String categoria : nuevoObservatorioForm.getCategoria()) {
					ps.setLong(1, idObservatory);
					ps.setLong(2, Long.parseLong(categoria));
					ps.addBatch();
				}
				ps.executeBatch();
			}
			ps = c.prepareStatement("INSERT INTO observatorio_ambito VALUES (?,?)");
			if (nuevoObservatorioForm.getAmbito() != null) {
				for (String ambito : nuevoObservatorioForm.getAmbito()) {
					ps.setLong(1, idObservatory);
					ps.setLong(2, Long.parseLong(ambito));
					ps.addBatch();
				}
				ps.executeBatch();
			}
			ps = c.prepareStatement("INSERT INTO observatorio_complejidad VALUES (?,?)");
			if (nuevoObservatorioForm.getComplejidad() != null) {
				for (String complejidad : nuevoObservatorioForm.getComplejidad()) {
					ps.setLong(1, idObservatory);
					ps.setLong(2, Long.parseLong(complejidad));
					ps.addBatch();
				}
				ps.executeBatch();
			}
			if ((idObservatory != 0) && (nuevoObservatorioForm.getAddSeeds() != null) && (!nuevoObservatorioForm.getAddSeeds().isEmpty())) {
				for (SemillaForm semillaForm : nuevoObservatorioForm.getAddSeeds()) {
					ps = c.prepareStatement("INSERT INTO observatorio_lista VALUES(?,?)");
					ps.setLong(1, idObservatory);
					ps.setLong(2, semillaForm.getId());
					ps.executeUpdate();
				}
			}
			List<SemillaForm> totalSeedsAdded = nuevoObservatorioForm.getAddSeeds() == null ? new ArrayList<SemillaForm>() : nuevoObservatorioForm.getAddSeeds();
			// add seeds to observatory
			if (nuevoObservatorioForm.getCategoria() != null) {
				for (String categoria : nuevoObservatorioForm.getCategoria()) {
					String[] tags = {};
					if (!org.apache.commons.lang3.StringUtils.isEmpty(nuevoObservatorioForm.getTagsString())) {
						tags = nuevoObservatorioForm.getTagsString().split(",");
					}
					totalSeedsAdded.addAll(SemillaDAO.getSeedsObservatory(c, Long.parseLong(categoria), Constants.NO_PAGINACION, tags));
				}
			}
			insertNewCrawlers(c, idObservatory, totalSeedsAdded);
			c.commit();
			return idObservatory;
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
	}

	/**
	 * Gets the observatory view.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @param page           the page
	 * @return the observatory view
	 * @throws Exception the SQL exception
	 */
	public static VerObservatorioForm getObservatoryView(Connection c, long idObservatorio, int page) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		ResultSet rs = null;
		PropertiesManager pmgr = new PropertiesManager();
		VerObservatorioForm verObservatorioForm = new VerObservatorioForm();
		try {
			ps = c.prepareStatement("SELECT DISTINCT(o.nombre) AS nombreObservatorio , o.id_guideline , " + "ca.* ,ot.*, p.nombre "
					+ "AS periodicidad, cl.nombre AS nombreCategoria, profundidad, amplitud, pseudoaleatorio, activo FROM observatorio o "
					+ "JOIN periodicidad p ON (p.id_periodicidad = o.id_periodicidad) " + "LEFT JOIN observatorio_categoria oc ON (o.id_observatorio = oc.id_observatorio) "
					+ "LEFT JOIN categorias_lista cl ON (cl.id_categoria = oc.id_categoria) " + "LEFT JOIN cartucho ca ON (o.id_cartucho = ca.id_cartucho) "
					+ "LEFT JOIN observatorio_tipo ot ON (o.id_tipo = ot.id_tipo) " + "WHERE o.id_observatorio = ? GROUP BY o.nombre;");
			ps.setLong(1, idObservatorio);
			rs = ps.executeQuery();
			if (rs.next()) {
				verObservatorioForm.setAmplitud(rs.getString("amplitud"));
				verObservatorioForm.setProfundidad(rs.getString("profundidad"));
				verObservatorioForm.setPseudoAleatorio(String.valueOf(rs.getBoolean("pseudoaleatorio")));
				verObservatorioForm.setNombre(rs.getString("nombreObservatorio"));
				verObservatorioForm.setPeriodicidad(rs.getString("periodicidad"));
				verObservatorioForm.setNormaAnalisis(rs.getLong("id_guideline"));
				verObservatorioForm.setActivo(rs.getBoolean("activo"));
				CartuchoForm cartucho = new CartuchoForm();
				cartucho.setId(rs.getLong("ca.id_cartucho"));
				cartucho.setName(rs.getString("ca.aplicacion"));
				verObservatorioForm.setCartucho(cartucho);
				ObservatoryTypeForm tipo = new ObservatoryTypeForm();
				tipo.setId(rs.getLong("ot.id_tipo"));
				tipo.setName(rs.getString("ot.name"));
				verObservatorioForm.setTipo(tipo);
				verObservatorioForm.setCategorias(getObservatoryCategories(c, idObservatorio));
			}
			DAOUtils.closeQueries(ps, rs);
			int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
			int resultFrom = pagSize * page;
			List<SemillaForm> semillasList = new ArrayList<>();
			ps = c.prepareStatement("SELECT * FROM lista l " + "JOIN observatorio_lista ol ON (l.id_lista = ol.id_lista) " + "JOIN observatorio o ON (o.id_observatorio = ol.id_observatorio) "
					+ "WHERE o.id_observatorio = ? LIMIT ? OFFSET ? ");
			ps.setLong(1, idObservatorio);
			ps.setInt(2, pagSize);
			ps.setInt(3, resultFrom);
			rs = ps.executeQuery();
			while (rs.next()) {
				SemillaForm semillaForm = new SemillaForm();
				semillaForm.setNombre(rs.getString("nombre"));
				semillaForm.setListaUrlsString(rs.getString("lista"));
				semillasList.add(semillaForm);
			}
			verObservatorioForm.setSemillasList(semillasList);
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return verObservatorioForm;
	}

	/**
	 * Gets the observatory form.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @return the observatory form
	 * @throws Exception the SQL exception
	 */
	public static ObservatorioForm getObservatoryForm(Connection c, final long idObservatorio) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final ObservatorioForm observatorioForm = new ObservatorioForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setAmplitud(rs.getLong("amplitud"));
					observatorioForm.setProfundidad(rs.getInt("profundidad"));
					observatorioForm.setNombre(rs.getString("nombre"));
					observatorioForm.setPeriodicidad(rs.getLong("id_periodicidad"));
					observatorioForm.setId_guideline(rs.getLong("id_guideline"));
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
					observatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
					observatorioForm.setLenguaje(rs.getLong("id_language"));
					observatorioForm.setTipo(rs.getLong("id_tipo"));
					observatorioForm.setTagsString(rs.getString("tags"));
					final CartuchoForm cartuchoForm = new CartuchoForm();
					cartuchoForm.setId(rs.getLong("id_cartucho"));
					observatorioForm.setCartucho(cartuchoForm);
					final List<CategoriaForm> selectedCategories = getObservatoryCategories(c, idObservatorio);
					observatorioForm.setCategoria(new String[selectedCategories.size()]);
					int i = 0;
					for (CategoriaForm categoria : selectedCategories) {
						observatorioForm.getCategoria()[i] = categoria.getId();
						i++;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return observatorioForm;
	}

	/**
	 * Gets the observatory form from execution.
	 *
	 * @param c                       the c
	 * @param idObservatorioExecution the id observatorio execution
	 * @return the observatory form from execution
	 * @throws Exception the SQL exception
	 */
	public static ObservatorioForm getObservatoryFormFromExecution(Connection c, long idObservatorioExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final ObservatorioForm observatorioForm = new ObservatorioForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio o JOIN observatorios_realizados ore " + "ON (o.id_observatorio = ore.id_observatorio) WHERE ore.id = ?;")) {
			ps.setLong(1, idObservatorioExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setAmplitud(rs.getLong("amplitud"));
					observatorioForm.setProfundidad(rs.getInt("profundidad"));
					observatorioForm.setNombre(rs.getString("nombre"));
					observatorioForm.setPeriodicidad(rs.getLong("id_periodicidad"));
					observatorioForm.setId_guideline(rs.getLong("id_guideline"));
					observatorioForm.setId(rs.getLong("id_observatorio"));
					observatorioForm.setFecha_inicio(rs.getTimestamp("fecha_inicio"));
					observatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
					observatorioForm.setLenguaje(rs.getLong("id_language"));
					observatorioForm.setTipo(rs.getLong("id_tipo"));
					CartuchoForm cartuchoForm = new CartuchoForm();
					cartuchoForm.setId(rs.getLong("id_cartucho"));
					observatorioForm.setCartucho(cartuchoForm);
				}
				return observatorioForm;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the observatory data to update.
	 *
	 * @param c                         the c
	 * @param modificarObservatorioForm the modificar observatorio form
	 * @return the observatory data to update
	 * @throws Exception the SQL exception
	 */
	public static ModificarObservatorioForm getObservatoryDataToUpdate(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio o WHERE o.id_observatorio = ?")) {
			ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					populateModificarObservatorioFormFromResultSet(c, modificarObservatorioForm, rs);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return modificarObservatorioForm;
	}

	/**
	 * Populate modificar observatorio form from result set.
	 *
	 * @param c                         the c
	 * @param modificarObservatorioForm the modificar observatorio form
	 * @param rs                        the rs
	 * @throws Exception the SQL exception
	 */
	private static void populateModificarObservatorioFormFromResultSet(Connection c, ModificarObservatorioForm modificarObservatorioForm, ResultSet rs) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		modificarObservatorioForm.setAmplitud(String.valueOf(rs.getInt("amplitud")));
		modificarObservatorioForm.setFecha(rs.getTimestamp("fecha_inicio"));
		modificarObservatorioForm.setNombre(rs.getString("nombre"));
		modificarObservatorioForm.setPeriodicidad(rs.getString("id_periodicidad"));
		modificarObservatorioForm.setProfundidad(String.valueOf(rs.getInt("profundidad")));
		modificarObservatorioForm.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
		modificarObservatorioForm.setNormaAnalisis(rs.getString("id_guideline"));
		modificarObservatorioForm.setActivo(rs.getBoolean("activo"));
		modificarObservatorioForm.setTagsString(rs.getString("tags"));
		final CartuchoForm cartuchoForm = new CartuchoForm();
		cartuchoForm.setId(rs.getLong("id_cartucho"));
		modificarObservatorioForm.setCartucho(cartuchoForm);
		final ObservatoryTypeForm tipo = new ObservatoryTypeForm();
		tipo.setId(rs.getLong("id_tipo"));
		modificarObservatorioForm.setTipo(tipo);
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		final Calendar cl1 = Calendar.getInstance();
		cl1.setTime(modificarObservatorioForm.getFecha());
		modificarObservatorioForm.setFechaInicio(df.format(cl1.getTime()));
		final String hour = leftPad(String.valueOf(cl1.get(Calendar.HOUR_OF_DAY)), 2, '0');
		final String minute = leftPad(String.valueOf(cl1.get(Calendar.MINUTE)), 2, '0');
		modificarObservatorioForm.setHoraInicio(hour);
		modificarObservatorioForm.setMinutoInicio(minute);
		modificarObservatorioForm.setSemillasAnadidas(getSeedsFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));
		modificarObservatorioForm.setSemillasNoAnadidas(getSeedsNotObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio())));
		final List<CategoriaForm> selectedCategories = getObservatoryCategories(c, Long.valueOf(modificarObservatorioForm.getId_observatorio()));
		modificarObservatorioForm.setCategoria(new String[selectedCategories.size()]);
		final List<String> categoriesId = new ArrayList<>(selectedCategories.size());
		for (CategoriaForm categoria : selectedCategories) {
			categoriesId.add(categoria.getId());
		}
		modificarObservatorioForm.setCategoria(categoriesId.toArray(new String[categoriesId.size()]));
		AmbitoForm ambitoForm = new AmbitoForm();
		ambitoForm.setId(rs.getString("id_ambito"));
		modificarObservatorioForm.setAmbitoForm(ambitoForm);
	}

	/**
	 * Gets the result seeds from observatory.
	 *
	 * @param c              the c
	 * @param searchForm     the search form
	 * @param idObservatorio the id observatorio
	 * @param idCategoria    the id categoria
	 * @param page           the page
	 * @return the result seeds from observatory
	 * @throws Exception the SQL exception
	 */
	public static List<ResultadoSemillaForm> getResultSeedsFromObservatory(Connection c, final SemillaForm searchForm, final Long idObservatorio, final Long idCategoria, final int page)
			throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final PropertiesManager pmgr = new PropertiesManager();
		final List<ResultadoSemillaForm> semillasFormList = new ArrayList<>();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"));
		final int resultFrom = pagSize * page;
		int paramCount = 1;
		String query = "SELECT l.id_lista, l.nombre, r.activo, r.id_rastreo, l.id_categoria, rr.id, rr.level, rr.score, l.id_complejidad FROM lista l "
				+ "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) " + "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) " + "WHERE id_obs_realizado = ? ";
		if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
			query += " AND l.lista like ?";
		}
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND l.nombre like ?";
		}
		if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
			query += " AND l.id_categoria = ?";
		}
		// Ordernar los resultados por categoría y nombre
		query += " ORDER BY l.id_categoria ASC, l.nombre ASC";
		if (page != Constants.NO_PAGINACION) {
			query += " LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(paramCount++, idObservatorio);
			if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
				ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
			}
			if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
				ps.setLong(paramCount++, idCategoria);
			}
			if (page != Constants.NO_PAGINACION) {
				ps.setInt(paramCount++, pagSize);
				ps.setInt(paramCount, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ResultadoSemillaForm resultadoSemillaForm = new ResultadoSemillaForm();
					resultadoSemillaForm.setId(rs.getString("l.id_lista"));
					resultadoSemillaForm.setName(rs.getString("l.nombre"));
					resultadoSemillaForm.setActive(rs.getBoolean("r.activo"));
					resultadoSemillaForm.setIdCrawling(rs.getString("r.id_rastreo"));
					resultadoSemillaForm.setIdCategory(rs.getLong("l.id_categoria"));
					resultadoSemillaForm.setIdFulfilledCrawling(rs.getString("rr.id"));
					resultadoSemillaForm.setScore(rs.getString("rr.score"));
					resultadoSemillaForm.setNivel(rs.getString("rr.level"));
					resultadoSemillaForm.setIdComplexity(rs.getLong("l.id_complejidad"));
					semillasFormList.add(resultadoSemillaForm);
					// Count URL crawled
					String numCrawlQuery = "SELECT count(ta.cod_url) as numCrawls  "
							+ "FROM tanalisis ta, rastreos_realizados rr, rastreo r, lista l WHERE ta.cod_rastreo = rr.id  and rr.id_rastreo = r.id_rastreo and r.semillas = l.id_lista  "
							+ "and ta.cod_rastreo in (select rr2.id from rastreos_realizados rr2 where rr2.id_obs_realizado=?) and rr.id = ?";
					PreparedStatement psCrawls = c.prepareStatement(numCrawlQuery);
					psCrawls.setLong(1, idObservatorio);
					psCrawls.setString(2, resultadoSemillaForm.getIdFulfilledCrawling());
					ResultSet rsCrawls = null;
					try {
						rsCrawls = psCrawls.executeQuery();
						if (rsCrawls.next()) {
							resultadoSemillaForm.setNumCrawls(rsCrawls.getInt("numCrawls"));
						}
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psCrawls, rsCrawls);
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return semillasFormList;
	}

	/**
	 * Devuelve toda la información de la semilla para que se pueda editar en un grid.
	 *
	 * @param c              the c
	 * @param searchForm     the search form
	 * @param idObservatorio the id observatorio
	 * @param idCategoria    the id categoria
	 * @param page           the page
	 * @return the result seeds full from observatory
	 * @throws Exception the SQL exception
	 */
	public static List<ResultadoSemillaFullForm> getResultSeedsFullFromObservatory(Connection c, final SemillaForm searchForm, final Long idObservatorio, final Long idCategoria, final int page)
			throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final PropertiesManager pmgr = new PropertiesManager();
		final List<ResultadoSemillaFullForm> semillasFormList = new ArrayList<>();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"));
		final int resultFrom = pagSize * page;
		int paramCount = 1;
		String numCrawlQuery = "(SELECT count(ta.cod_url) "
				+ "FROM tanalisis ta, rastreos_realizados rr3, rastreo r2, lista l2 WHERE ta.cod_rastreo = rr3.id  and rr3.id_rastreo = r2.id_rastreo and r2.semillas = l2.id_lista  "
				+ "and ta.cod_rastreo in (select rr2.id from rastreos_realizados rr2 where rr2.id_obs_realizado=" + idObservatorio + ") and rr3.id = rr.id) as numCrawls";
		String query = "SELECT l.id_lista, l.nombre, l.acronimo ,l.activa, l.in_directory, l.lista, r.activo, cl.nombre as categoriaNombre, cl.orden , r.id_rastreo, "
				+ "l.id_categoria, l.id_ambito, l.id_complejidad, rr.id, al.nombre, cxl.nombre, cxl.profundidad, cxl.amplitud, rr.score, rr.level as nivel, l.observaciones,  " + numCrawlQuery
				+ " FROM lista l " + "LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) " + "LEFT JOIN ambitos_lista al ON(l.id_ambito = al.id_ambito) "
				+ "LEFT JOIN complejidades_lista cxl ON(l.id_complejidad = cxl.id_complejidad) " + "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) "
				+ "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) " + "WHERE id_obs_realizado = ? ";
		if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
			query += " AND l.lista like ?";
		}
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?)";
		}
		if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
			query += " AND l.id_categoria = ?";
		}
		// Ordernar los resultados por categoría y nombre
		if (StringUtils.isNotEmpty(searchForm.getSortCol()) && StringUtils.isNotEmpty(searchForm.getSortOrder())) {
			if ("complejidad".equalsIgnoreCase(searchForm.getSortCol())) {
				query += " ORDER BY cxl.nombre " + searchForm.getSortOrder() + ", l.id_lista";
			} else if ("score".equalsIgnoreCase(searchForm.getSortCol())) {
				query += " ORDER BY cast(rr.score as DECIMAL(10,5)) " + searchForm.getSortOrder() + ", l.id_lista";
			} else {
				query += " ORDER BY " + searchForm.getSortCol() + " " + searchForm.getSortOrder() + ", l.id_lista";
			}
		} else {
			query += " ORDER BY l.id_categoria ASC, l.nombre ASC";
		}
		if (page != Constants.NO_PAGINACION) {
			query += " LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(paramCount++, idObservatorio);
			if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
				ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
			}
			if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
				ps.setLong(paramCount++, idCategoria);
			}
			if (page != Constants.NO_PAGINACION) {
				ps.setInt(paramCount++, pagSize);
				ps.setInt(paramCount, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ResultadoSemillaFullForm resultadoSemillaForm = new ResultadoSemillaFullForm();
					resultadoSemillaForm.setId(rs.getString("l.id_lista"));
					resultadoSemillaForm.setNombre(rs.getString("l.nombre"));
					resultadoSemillaForm.setAcronimo(rs.getString("l.acronimo"));
					resultadoSemillaForm.setListaUrls(convertStringToList(rs.getString("l.lista")));
					resultadoSemillaForm.setScore(rs.getString("rr.score"));
					resultadoSemillaForm.setNivel(rs.getString("nivel"));
					resultadoSemillaForm.setObservaciones(rs.getString("l.observaciones"));
					if (rs.getLong("l.activa") == 0) {
						resultadoSemillaForm.setActiva(false);
					} else {
						resultadoSemillaForm.setActiva(true);
					}
					if (rs.getLong("l.in_directory") == 0) {
						resultadoSemillaForm.setInDirectory(false);
					} else {
						resultadoSemillaForm.setInDirectory(true);
					}
					resultadoSemillaForm.setIdCrawling(rs.getString("r.id_rastreo"));
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString("l.id_categoria"));
					categoriaForm.setName(rs.getString("categoriaNombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					resultadoSemillaForm.setCategoria(categoriaForm);
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString("l.id_ambito"));
					ambitoForm.setName(rs.getString("al.nombre"));
					resultadoSemillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString("l.id_complejidad"));
					complejidadForm.setName(rs.getString("cxl.nombre"));
					complejidadForm.setProfundidad(rs.getInt("cxl.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					resultadoSemillaForm.setComplejidad(complejidadForm);
					// resultadoSemillaForm.setIdCategory(rs.getLong("l.id_categoria"));
					resultadoSemillaForm.setIdFulfilledCrawling(rs.getString("rr.id"));
					resultadoSemillaForm.setNumCrawls(rs.getInt("numCrawls"));
					// Cargar las dependencias de la semilla
					PreparedStatement psDependencias = c
							.prepareStatement("SELECT id_dependencia, nombre FROM dependencia WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?)");
					psDependencias.setString(1, resultadoSemillaForm.getId());
					List<DependenciaForm> listDependencias = new ArrayList<>();
					ResultSet rsDependencias = null;
					try {
						rsDependencias = psDependencias.executeQuery();
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString("nombre"));
							listDependencias.add(dependencia);
						}
						resultadoSemillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psDependencias, rsDependencias);
					}
					// Load tags
					PreparedStatement psEtiquetas = c.prepareStatement(
							"SELECT e.id_etiqueta, e.nombre FROM etiqueta e WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?) ORDER BY UPPER(e.nombre)");
					psEtiquetas.setString(1, resultadoSemillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					ResultSet rsEtiquetas = null;
					try {
						rsEtiquetas = psEtiquetas.executeQuery();
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString("nombre"));
							listEtiquetas.add(etiqueta);
						}
						resultadoSemillaForm.setEtiquetas(listEtiquetas);
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
					}
					semillasFormList.add(resultadoSemillaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return semillasFormList;
	}

	/**
	 * Gets the finish crawler ids from seed and observatory without analisis.
	 *
	 * @param c              the c
	 * @param idObservatory  the id observatory
	 * @param idObsRealizado the id obs realizado
	 * @return the finish crawler ids from seed and observatory without analisis
	 * @throws Exception the exception
	 */
	public static List<Long> getFinishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis(Connection c, Long idObservatory, Long idObsRealizado) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<Long> crawlerIds = new ArrayList<>();
		// Union de rastreos no realizados y rastreos empezados pero no
		// terminados (<> estado 4)
		String query = "SELECT DISTINCT u.id_rastreo  FROM ("
				+ "(SELECT r.id_rastreo FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo NOT IN (SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE id_obs_realizado = ? ) AND r.activo = 1) "
				+ "UNION ALL "
				+ "(SELECT r.id_rastreo FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo IN (SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE rr.id_obs_realizado = ? AND rr.id not IN (select ta.cod_rastreo as id_rastreo from tanalisis ta)))"
				+ ") u ORDER BY u.id_rastreo ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObsRealizado);
			ps.setLong(3, idObservatory);
			ps.setLong(4, idObsRealizado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("id_rastreo"));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return crawlerIds;
	}

	/**
	 * Gets the finish crawler ids from seed and observatory with less results threshold.
	 *
	 * @param c              the c
	 * @param idObsRealizado the id obs realizado
	 * @param percent        the percent
	 * @param seeds          the seeds
	 * @return the finish crawler ids from seed and observatory with less results threshold
	 * @throws Exception the exception
	 */
	public static List<Long> getFinishCrawlerIdsFromSeedAndObservatoryWithLessResultsThreshold(Connection c, final Long idObsRealizado, final Integer percent, final Integer seeds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<Long> crawlerIds = new ArrayList<>();
		final String tresholdCalculation = "(((cl.amplitud*cl.profundidad)+1)*(((select `value` from observatorio_extra_configuration where `key` ='umbral'))/100))";
		String query = "SELECT ID_SEED,ID_RR,ID_R, NUM_C, ((cl.amplitud*cl.profundidad)+1) CX, " + tresholdCalculation
				+ " THRESHOLD  FROM (SELECT l.id_lista as ID_SEED, rr.id as ID_RR, rr.id_rastreo as ID_R , count(ta.cod_url) as NUM_C FROM tanalisis ta, rastreos_realizados rr, rastreo r, lista l WHERE ta.cod_rastreo = rr.id AND rr.id_rastreo = r.id_rastreo and r.semillas = l.id_lista AND r.estado = 4 and ta.cod_rastreo in (select rr2.id from rastreos_realizados rr2 where rr2.id_obs_realizado= ?) GROUP by rr.id) AS NUM_CRAWLS, lista l2, complejidades_lista cl WHERE  l2.id_lista = ID_SEED AND cl.id_complejidad=l2.id_complejidad";
		if (percent == null && seeds == null) {
			query += " AND NUM_C < " + tresholdCalculation + "";
		} else {
			if (percent != null) {
				query += " AND  ((NUM_C * 1.0) / ((cl.profundidad * cl.amplitud) + 1)) * 100 <= ?";
			}
			if (seeds != null) {
				query += " AND NUM_C" + "<= ?";
			}
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			int paramNumber = 1;
			ps.setLong(paramNumber, idObsRealizado);
			paramNumber++;
			if (percent != null) {
				ps.setInt(paramNumber, percent);
				paramNumber++;
			}
			if (seeds != null) {
				ps.setInt(paramNumber, seeds);
				paramNumber++;
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("ID_R"));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return crawlerIds;
	}

	/**
	 * Gets the result seeds full from observatory by ids.
	 *
	 * @param c              the c
	 * @param searchForm     the search form
	 * @param idObservatorio the id observatorio
	 * @param idCategoria    the id categoria
	 * @param page           the page
	 * @param crawlIds       the crawl ids
	 * @return the result seeds full from observatory by ids
	 * @throws Exception the SQL exception
	 */
	public static List<ResultadoSemillaFullForm> getResultSeedsFullFromObservatoryByIds(Connection c, final SemillaForm searchForm, final Long idObservatorio, final Long idCategoria, final int page,
			final List<Long> crawlIds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final PropertiesManager pmgr = new PropertiesManager();
		final List<ResultadoSemillaFullForm> semillasFormList = new ArrayList<>();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"));
		final int resultFrom = pagSize * page;
		int paramCount = 1;
		String query = "SELECT l.id_lista, l.nombre, l.acronimo ,l.activa, l.in_directory, l.lista, r.activo, cl.nombre, cl.orden , r.id_rastreo, l.id_categoria, l.id_ambito, l.id_complejidad,l.observaciones, rr.id, al.nombre, cxl.nombre, cxl.profundidad, cxl.amplitud, rr.score, rr.level FROM lista l "
				+ "LEFT JOIN categorias_lista cl ON(l.id_categoria = cl.id_categoria) " + "LEFT JOIN ambitos_lista al ON(l.id_ambito = al.id_ambito) "
				+ "LEFT JOIN complejidades_lista cxl ON(l.id_complejidad = cxl.id_complejidad) " + "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) "
				+ "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) " + "WHERE id_obs_realizado = ? ";
		if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
			query += " AND l.lista like ?";
		}
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND UPPER(l.nombre) like UPPER(?)";
		}
		if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
			query += " AND l.id_categoria = ?";
		}
		if (crawlIds != null && !crawlIds.isEmpty()) {
			String temp = " AND r.id_rastreo IN (";
			for (int i = 0; i < crawlIds.size(); i++) {
				temp += ",?";
			}
			temp = temp.replaceFirst(",", "");
			temp += ")";
			query = query + temp;
		}
		// Ordernar los resultados por categoría y nombre
		query += " ORDER BY l.id_categoria ASC, l.nombre ASC";
		if (page != Constants.NO_PAGINACION) {
			query += " LIMIT ? OFFSET ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(paramCount++, idObservatorio);
			if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
				ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
			}
			if (idCategoria != Constants.COMPLEXITY_SEGMENT_NONE) {
				ps.setLong(paramCount++, idCategoria);
			}
			if (page != Constants.NO_PAGINACION) {
				ps.setInt(paramCount++, pagSize);
				ps.setInt(paramCount, resultFrom);
			}
			if (crawlIds != null && !crawlIds.isEmpty()) {
				for (int i = 0; i < crawlIds.size(); i++) {
					ps.setLong(paramCount++, crawlIds.get(i));
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final ResultadoSemillaFullForm resultadoSemillaForm = new ResultadoSemillaFullForm();
					resultadoSemillaForm.setId(rs.getString("l.id_lista"));
					resultadoSemillaForm.setNombre(rs.getString("l.nombre"));
					resultadoSemillaForm.setAcronimo(rs.getString("l.acronimo"));
					resultadoSemillaForm.setListaUrls(convertStringToList(rs.getString("l.lista")));
					resultadoSemillaForm.setScore(rs.getString("rr.score"));
					resultadoSemillaForm.setNivel(rs.getString("rr.level"));
					resultadoSemillaForm.setObservaciones(rs.getString("l.observaciones"));
					if (rs.getLong("l.activa") == 0) {
						resultadoSemillaForm.setActiva(false);
					} else {
						resultadoSemillaForm.setActiva(true);
					}
					if (rs.getLong("l.in_directory") == 0) {
						resultadoSemillaForm.setInDirectory(false);
					} else {
						resultadoSemillaForm.setInDirectory(true);
					}
					resultadoSemillaForm.setIdCrawling(rs.getString("r.id_rastreo"));
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString("l.id_categoria"));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					resultadoSemillaForm.setCategoria(categoriaForm);
					final AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString("l.id_ambito"));
					ambitoForm.setName(rs.getString("al.nombre"));
					resultadoSemillaForm.setAmbito(ambitoForm);
					final ComplejidadForm complejidadForm = new ComplejidadForm();
					complejidadForm.setId(rs.getString("l.id_complejidad"));
					complejidadForm.setName(rs.getString("cxl.nombre"));
					complejidadForm.setProfundidad(rs.getInt("cxl.profundidad"));
					complejidadForm.setAmplitud(rs.getInt("cxl.amplitud"));
					resultadoSemillaForm.setComplejidad(complejidadForm);
					// resultadoSemillaForm.setIdCategory(rs.getLong("l.id_categoria"));
					resultadoSemillaForm.setIdFulfilledCrawling(rs.getString("rr.id"));
					// Cargar las dependencias de la semilla
					PreparedStatement psDependencias = c
							.prepareStatement("SELECT id_dependencia, nombre FROM dependencia WHERE id_dependencia in (SELECT id_dependencia FROM semilla_dependencia WHERE id_lista = ?)");
					psDependencias.setString(1, resultadoSemillaForm.getId());
					List<DependenciaForm> listDependencias = new ArrayList<>();
					ResultSet rsDependencias = null;
					try {
						rsDependencias = psDependencias.executeQuery();
						while (rsDependencias.next()) {
							DependenciaForm dependencia = new DependenciaForm();
							dependencia.setId(rsDependencias.getLong("id_dependencia"));
							dependencia.setName(rsDependencias.getString("nombre"));
							listDependencias.add(dependencia);
						}
						resultadoSemillaForm.setDependencias(listDependencias);
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psDependencias, rsDependencias);
					}
					// Cargar las etiquetas de la semilla
					PreparedStatement psEtiquetas = c.prepareStatement("SELECT id_etiqueta, nombre FROM etiqueta WHERE id_etiqueta in (SELECT id_etiqueta FROM semilla_etiqueta WHERE id_lista = ?)");
					psEtiquetas.setString(1, resultadoSemillaForm.getId());
					List<EtiquetaForm> listEtiquetas = new ArrayList<>();
					ResultSet rsEtiquetas = null;
					try {
						rsEtiquetas = psEtiquetas.executeQuery();
						while (rsEtiquetas.next()) {
							EtiquetaForm etiqueta = new EtiquetaForm();
							etiqueta.setId(rsEtiquetas.getLong("id_etiqueta"));
							etiqueta.setName(rsEtiquetas.getString("nombre"));
							listEtiquetas.add(etiqueta);
						}
						resultadoSemillaForm.setEtiquetas(listEtiquetas);
						semillasFormList.add(resultadoSemillaForm);
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psEtiquetas, rsEtiquetas);
					}
					// Count URL crawled
					String numCrawlQuery = "SELECT count(ta.cod_url) as numCrawls  "
							+ "FROM tanalisis ta, rastreos_realizados rr, rastreo r, lista l WHERE ta.cod_rastreo = rr.id  and rr.id_rastreo = r.id_rastreo and r.semillas = l.id_lista  "
							+ "and ta.cod_rastreo in (select rr2.id from rastreos_realizados rr2 where rr2.id_obs_realizado=?) and rr.id = ?";
					PreparedStatement psCrawls = c.prepareStatement(numCrawlQuery);
					psCrawls.setLong(1, idObservatorio);
					psCrawls.setString(2, resultadoSemillaForm.getIdFulfilledCrawling());
					ResultSet rsCrawls = null;
					try {
						rsCrawls = psCrawls.executeQuery();
						if (rsCrawls.next()) {
							resultadoSemillaForm.setNumCrawls(rsCrawls.getInt("numCrawls"));
							// Calculate percent
							if (rsCrawls.getInt("numCrawls") > 0) {
								resultadoSemillaForm.setPercentNumCrawls(((rsCrawls.getInt("numCrawls") * 1.0) / ((rs.getInt("cxl.profundidad") * rs.getInt("cxl.amplitud")) + 1)) * 100);
							}
						}
					} catch (SQLException e) {
						Logger.putLog("SQL Exception: ", SemillaDAO.class, Logger.LOG_LEVEL_ERROR, e);
						throw e;
					} finally {
						DAOUtils.closeQueries(psCrawls, rsCrawls);
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return semillasFormList;
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
	 * Count result seeds from observatory.
	 *
	 * @param c                      the c
	 * @param searchForm             the search form
	 * @param idExecutionObservatory the id execution observatory
	 * @param idCategory             the id category
	 * @return the int
	 * @throws Exception the SQL exception
	 */
	public static int countResultSeedsFromObservatory(Connection c, SemillaForm searchForm, long idExecutionObservatory, long idCategory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int paramCount = 1;
		String query = "SELECT COUNT(*) FROM lista l " + "LEFT JOIN rastreos_realizados rr ON (rr.id_lista = l.id_lista) " + "LEFT JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) "
				+ "WHERE id_obs_realizado = ? ";
		if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
			query += " AND l.lista like ?";
		}
		if (StringUtils.isNotEmpty(searchForm.getNombre())) {
			query += " AND l.nombre like ?";
		}
		if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
			query += " AND l.id_categoria = ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(paramCount++, idExecutionObservatory);
			if (StringUtils.isNotEmpty(searchForm.getListaUrlsString())) {
				ps.setString(paramCount++, "%" + searchForm.getListaUrlsString() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getNombre())) {
				ps.setString(paramCount++, "%" + searchForm.getNombre() + "%");
			}
			if (idCategory != Constants.COMPLEXITY_SEGMENT_NONE) {
				ps.setLong(paramCount, idCategory);
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the seeds from observatory.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @return the seeds from observatory
	 * @throws Exception the SQL exception
	 */
	private static List<SemillaForm> getSeedsFromObservatory(Connection c, long idObservatorio) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<SemillaForm> semillasFormList = new ArrayList<>();
		try (PreparedStatement ps = c
				.prepareStatement("SELECT * FROM lista l " + "LEFT JOIN observatorio_lista ol ON (ol.id_lista = l.id_lista) " + "WHERE ol.id_observatorio = ? AND l.id_categoria IS NULL")) {
			ps.setLong(1, idObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong("id_lista"));
					semillaForm.setNombre(rs.getString("nombre"));
					semillasFormList.add(semillaForm);
				}
			}
			return semillasFormList;
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the seeds not observatory.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @return the seeds not observatory
	 * @throws Exception the SQL exception
	 */
	private static List<SemillaForm> getSeedsNotObservatory(Connection c, long idObservatorio) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<SemillaForm> semillasFormList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM lista l WHERE l.id_tipo_lista = ? AND l.id_categoria IS NULL " + "AND (l.id_lista NOT IN ("
				+ "SELECT ol.id_lista FROM observatorio_lista ol " + "WHERE ol.id_observatorio = ?)) ")) {
			ps.setLong(1, Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
			ps.setLong(2, idObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setId(rs.getLong("id_lista"));
					semillaForm.setNombre(rs.getString("nombre"));
					semillasFormList.add(semillaForm);
				}
			}
			return semillasFormList;
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update observatory crawlings.
	 *
	 * @param c              the c
	 * @param newObservatory the new observatory
	 * @param oldObservatory the old observatory
	 * @throws Exception the SQL exception
	 */
	private static void updateObservatoryCrawlings(Connection c, final ModificarObservatorioForm newObservatory, final ObservatorioForm oldObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<String> newCategories = Arrays.asList(newObservatory.getCategoria());
		final List<String> oldCategories = Arrays.asList(oldObservatory.getCategoria());
		// Desactivamos las categorías antiguas que no están entre las nuevas
		for (String oldCategory : oldCategories) {
			if (!newCategories.contains(oldCategory)) {
				final List<Long> crawlerIds = ObservatorioDAO.getCrawlerFromCategory(c, Long.parseLong(oldCategory));
				for (Long idCrawler : crawlerIds) {
					RastreoDAO.enableDisableCrawler(c, idCrawler, false);
				}
			}
		}
		// Añadimos las categorías nuevas que no están entre las viejas
		for (String newCategory : newCategories) {
			final List<SemillaForm> seedsFromCategory = new ArrayList<>();
			if (!oldCategories.contains(newCategory)) {
				seedsFromCategory.addAll(SemillaDAO.getSeedsByCategory(c, Long.parseLong(newCategory), Constants.NO_PAGINACION, new SemillaForm()));
			}
			insertNewCrawlers(c, oldObservatory.getId(), seedsFromCategory);
		}
	}

	/**
	 * Update observatory.
	 *
	 * @param c                         the c
	 * @param modificarObservatorioForm the modificar observatorio form
	 * @return the modificar observatorio form
	 * @throws Exception the exception
	 */
	public static ModificarObservatorioForm updateObservatory(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			// Actualizamos los rastreos
			ObservatorioForm oldObservatory = getObservatoryForm(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
			updateObservatoryCrawlings(c, modificarObservatorioForm, oldObservatory);
			ps = c.prepareStatement(
					"UPDATE observatorio SET Nombre = ?, Id_Periodicidad = ?, Profundidad = ?, Amplitud = ?, Fecha_Inicio = ?, id_guideline = ?, pseudoaleatorio = ?, id_language = ?, id_cartucho = ?, activo = ?, id_tipo = ?, id_ambito = ?, tags = ? WHERE id_observatorio = ?");
			ps.setString(1, modificarObservatorioForm.getNombre());
			ps.setInt(2, Integer.parseInt(modificarObservatorioForm.getPeriodicidad()));
			ps.setInt(3, Integer.parseInt(modificarObservatorioForm.getProfundidad()));
			ps.setInt(4, Integer.parseInt(modificarObservatorioForm.getAmplitud()));
			modificarObservatorioForm
					.setFecha(CrawlerUtils.getFechaInicio(modificarObservatorioForm.getFechaInicio(), modificarObservatorioForm.getHoraInicio(), modificarObservatorioForm.getMinutoInicio()));
			ps.setTimestamp(5, new Timestamp(modificarObservatorioForm.getFecha().getTime()));
			ps.setLong(6, CartuchoDAO.getGuideline(c, modificarObservatorioForm.getCartucho().getId()));
			ps.setBoolean(7, modificarObservatorioForm.isPseudoAleatorio());
			ps.setLong(8, modificarObservatorioForm.getLenguaje());
			ps.setLong(9, modificarObservatorioForm.getCartucho().getId());
			ps.setBoolean(10, modificarObservatorioForm.isActivo());
			ps.setLong(11, modificarObservatorioForm.getTipo().getId());
			ps.setString(12, modificarObservatorioForm.getAmbitoForm().getId());
			ps.setString(13, modificarObservatorioForm.getTagsString());
			ps.setLong(14, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
			ps.executeUpdate();
			DAOUtils.closeQueries(ps, null);
			// Rehacemos las asociaciones con las categorías
			ps = c.prepareStatement("DELETE FROM observatorio_categoria WHERE id_observatorio = ?");
			ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
			ps.executeUpdate();
			DAOUtils.closeQueries(ps, null);
			ps = c.prepareStatement("INSERT INTO observatorio_categoria VALUES (?,?)");
			if (modificarObservatorioForm.getCategoria() != null) {
				for (String categoria : modificarObservatorioForm.getCategoria()) {
					ps.setLong(1, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
					ps.setLong(2, Long.parseLong(categoria));
					ps.addBatch();
				}
				ps.executeBatch();
			}
//			editCrawlings(c, modificarObservatorioForm);
			disableCrawlers(c, modificarObservatorioForm.getSemillasNoAnadidas(), modificarObservatorioForm.getId_observatorio());
//			deleteSeedAssociation(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
//			addObservatorySeeds(c, modificarObservatorioForm.getSemillasAnadidas(), Long.parseLong(modificarObservatorioForm.getId_observatorio()));
//			// Insertamos los rastreos independientes de la categoría asociada
//			insertNewCrawlers(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()), modificarObservatorioForm.getSemillasAnadidas());
			List<SemillaForm> totalSeedsAdded = new ArrayList<SemillaForm>();
			// add seeds to observatory
			if (modificarObservatorioForm.getCategoria() != null) {
				for (String categoria : modificarObservatorioForm.getCategoria()) {
					String[] tags = {};
					if (!org.apache.commons.lang3.StringUtils.isEmpty(modificarObservatorioForm.getTagsString())) {
						tags = modificarObservatorioForm.getTagsString().split(",");
					}
					totalSeedsAdded.addAll(SemillaDAO.getSeedsObservatory(c, Long.parseLong(categoria), Constants.NO_PAGINACION, tags));
				}
			}
			deleteSeedAssociation(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
			updateCrawlers(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()), totalSeedsAdded);
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
		return modificarObservatorioForm;
	}

	/**
	 * Put data to insert.
	 *
	 * @param insertarRastreoForm the insertar rastreo form
	 * @param observatorioForm    the observatorio form
	 */
	public static void putDataToInsert(final InsertarRastreoForm insertarRastreoForm, final ObservatorioForm observatorioForm) {
		insertarRastreoForm.setId_observatorio(observatorioForm.getId());
		insertarRastreoForm.setCartucho(observatorioForm.getCartucho().getId().toString());
		insertarRastreoForm.setNormaAnalisis(String.valueOf(observatorioForm.getId_guideline()));
		insertarRastreoForm.setProfundidad(observatorioForm.getProfundidad());
		insertarRastreoForm.setTopN(observatorioForm.getAmplitud());
		insertarRastreoForm.setPseudoAleatorio(observatorioForm.isPseudoAleatorio());
		insertarRastreoForm.setLenguaje(observatorioForm.getLenguaje());
	}

	/**
	 * Insert new crawlers.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param seeds         the seeds
	 * @throws Exception the SQL exception
	 */
	public static void insertNewCrawlers(Connection c, final long idObservatory, final List<SemillaForm> seeds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
		final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
		putDataToInsert(insertarRastreoForm, observatorioForm);
		if (seeds != null) {
			for (SemillaForm semillaForm : seeds) {
				insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + SemillaDAO.getSeedById(c, semillaForm.getId()).getNombre());
				insertarRastreoForm.setId_semilla(semillaForm.getId());
				insertarRastreoForm.setId_observatorio(idObservatory);
				insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
				insertarRastreoForm.setLenguaje(observatorioForm.getLenguaje());
				final Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, idObservatory, semillaForm.getId());
				if (idCrawler == -1) {
					insertarRastreoForm.setActive(semillaForm.isActiva());
					RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
					// Desactivamos si la semilla está desactivada o borrada
					if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
						RastreoDAO.enableDisableCrawler(c, idCrawler, false);
					} else {
						RastreoDAO.enableDisableCrawler(c, idCrawler, true);
					}
				} else {
					// Desactivamos si la semilla está desactivada o borrada
					if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
						RastreoDAO.enableDisableCrawler(c, idCrawler, false);
					} else {
						RastreoDAO.enableDisableCrawler(c, idCrawler, true);
					}
				}
			}
		}
	}

	/**
	 * Update crawlers.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param seeds         the seeds
	 * @throws Exception the SQL exception
	 */
	public static void updateCrawlers(Connection c, final long idObservatory, final List<SemillaForm> seeds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		// Disable all
		try (PreparedStatement ps = c.prepareStatement("UPDATE rastreo SET activo = 0 WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatory);
			ps.executeUpdate();
			// Insert new or update existing
			final InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
			final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			if (seeds != null) {
				for (SemillaForm semillaForm : seeds) {
					insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + SemillaDAO.getSeedById(c, semillaForm.getId()).getNombre());
					insertarRastreoForm.setId_semilla(semillaForm.getId());
					insertarRastreoForm.setId_observatorio(idObservatory);
					insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
					insertarRastreoForm.setProfundidad(observatorioForm.getProfundidad());
					insertarRastreoForm.setTopN(observatorioForm.getAmplitud());
					insertarRastreoForm.setCartucho(String.valueOf(observatorioForm.getCartucho().getId()));
					insertarRastreoForm.setNormaAnalisis(String.valueOf(observatorioForm.getCartucho().getId()));
					insertarRastreoForm.setLenguaje(observatorioForm.getLenguaje());
					final Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, idObservatory, semillaForm.getId());
					if (idCrawler == -1) {
						insertarRastreoForm.setActive(semillaForm.isActiva());
						RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
						// Desactivamos si la semilla está desactivada o borrada
						if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
							RastreoDAO.enableDisableCrawler(c, idCrawler, false);
						} else {
							RastreoDAO.enableDisableCrawler(c, idCrawler, true);
						}
					} else {
						// Desactivamos si la semilla está desactivada o borrada
						RastreoDAO.updateRastreo(c, false, insertarRastreoForm, idCrawler);
						if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
							RastreoDAO.enableDisableCrawler(c, idCrawler, false);
						} else {
							RastreoDAO.enableDisableCrawler(c, idCrawler, true);
						}
					}
				}
			}
		}
	}

	/**
	 * Disable crawlers.
	 *
	 * @param c             the c
	 * @param seeds         the seeds
	 * @param idObservatory the id observatory
	 * @throws Exception the SQL exception
	 */
	private static void disableCrawlers(Connection c, List<SemillaForm> seeds, String idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		for (SemillaForm semillaForm : seeds) {
			// Desactivamos los rastreos de las semilla sque han sido
			// desasignadas al observatorio o
			Long idCrawler = ObservatorioDAO.existObservatoryCrawl(c, Long.parseLong(idObservatory), semillaForm.getId());
			if (idCrawler != -1) {
				// RastreoDAO.enableDisableCrawler(c, idCrawler, false);
				if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
					RastreoDAO.enableDisableCrawler(c, idCrawler, false);
				} else {
					RastreoDAO.enableDisableCrawler(c, idCrawler, true);
				}
			}
		}
	}

	/**
	 * Delete seed association.
	 *
	 * @param c              the c
	 * @param idObservatorio the id observatorio
	 * @throws Exception the SQL exception
	 */
	public static void deleteSeedAssociation(Connection c, long idObservatorio) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_lista WHERE id_observatorio = ?")) {
			ps.setLong(1, idObservatorio);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Adds the observatory seeds.
	 *
	 * @param c             the c
	 * @param seeds         the seeds
	 * @param idObservatory the id observatory
	 * @throws Exception the SQL exception
	 */
	private static void addObservatorySeeds(Connection c, final List<SemillaForm> seeds, final long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO observatorio_lista VALUES(?,?)")) {
			for (SemillaForm semillaForm : seeds) {
				ps.setLong(1, idObservatory);
				ps.setLong(2, semillaForm.getId());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			Logger.putLog("Error", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the observatory execution ids.
	 *
	 * @param c             the c
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param cartridgeId   the cartridge id
	 * @return the observatory execution ids
	 * @throws Exception the SQL exception
	 */
	public static Map<Long, Date> getObservatoryExecutionIds(Connection c, long observatoryId, long executionId, long cartridgeId) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final Map<Long, Date> observatoryExecutionIdsList = new HashMap<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final String limit = pmgr.getValue(CRAWLER_PROPERTIES, "observatory.evolution.limit").trim();
		try (PreparedStatement ps = c.prepareStatement("SELECT id,fecha FROM observatorios_realizados"
				+ " WHERE id_observatorio = ? AND id_cartucho = ? AND fecha <= (SELECT fecha FROM observatorios_realizados WHERE id = ?)" + " ORDER BY fecha DESC LIMIT ?;")) {
			ps.setLong(1, observatoryId);
			ps.setLong(2, cartridgeId);
			ps.setLong(3, executionId);
			ps.setInt(4, Integer.parseInt(limit));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					observatoryExecutionIdsList.put(rs.getLong("id"), rs.getTimestamp("fecha"));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return observatoryExecutionIdsList;
	}

	/**
	 * Exist observatory crawl.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param idSeed        the id seed
	 * @return the long
	 * @throws Exception the SQL exception
	 */
	private static long existObservatoryCrawl(Connection c, final long idObservatory, final long idSeed) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " + "JOIN lista l ON (r.semillas = l.id_lista) "
				+ "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " + "WHERE l.id_lista = ? AND o.id_observatorio = ?")) {
			ps.setLong(1, idSeed);
			ps.setLong(2, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getLong("id_rastreo") : -1;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Comprobamos que si existe un observatorio con un determinado nombre.
	 *
	 * @param c               conexión Connection a la BD
	 * @param observatoryName cadena String con el nombre del observatorio
	 * @return true si existe un observatorio con ese nombre o false en caso contrario
	 * @throws Exception the SQL exception
	 */
	public static boolean existObservatory(Connection c, String observatoryName) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM observatorio WHERE nombre = ?")) {
			ps.setString(1, observatoryName);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the crawler from category.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the crawler from category
	 * @throws Exception the SQL exception
	 */
	private static List<Long> getCrawlerFromCategory(Connection c, final long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<Long> idCrawlerList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " + "JOIN lista l ON (r.semillas = l.id_lista) " + "WHERE l.id_categoria = ?")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					idCrawlerList.add(rs.getLong("id_rastreo"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return idCrawlerList;
	}

	/**
	 * Gets the crawler from observatory.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the crawler from observatory
	 * @throws Exception the SQL exception
	 */
	private static List<Long> getCrawlerFromObservatory(Connection c, long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<Long> idCrawlerList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT r.id_rastreo FROM rastreo r " + "JOIN observatorio o ON (o.id_observatorio = r.id_observatorio) " + "WHERE o.id_observatorio = ?")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					idCrawlerList.add(rs.getLong("id_rastreo"));
				}
			}
			return idCrawlerList;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Edits the crawlings.
	 *
	 * @param c                         the c
	 * @param modificarObservatorioForm the modificar observatorio form
	 * @throws Exception the SQL exception
	 */
	private static void editCrawlings(Connection c, ModificarObservatorioForm modificarObservatorioForm) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
		insertarRastreoForm.setProfundidad(Integer.parseInt(modificarObservatorioForm.getProfundidad()));
		insertarRastreoForm.setTopN(Long.parseLong(modificarObservatorioForm.getAmplitud()));
		insertarRastreoForm.setCartucho(modificarObservatorioForm.getCartucho().getId().toString());
		insertarRastreoForm.setNormaAnalisis(modificarObservatorioForm.getNormaAnalisis());
		insertarRastreoForm.setPseudoAleatorio(modificarObservatorioForm.isPseudoAleatorio());
		insertarRastreoForm.setLenguaje(modificarObservatorioForm.getLenguaje());
		List<Long> idCrawlerList = getCrawlerFromObservatory(c, Long.parseLong(modificarObservatorioForm.getId_observatorio()));
		for (Long idCrawler : idCrawlerList) {
			SemillaForm semillaForm = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idCrawler));
			insertarRastreoForm.setInDirectory(semillaForm.isInDirectory());
			insertarRastreoForm.setCodigo(modificarObservatorioForm.getNombre() + "-" + semillaForm.getNombre());
			insertarRastreoForm.setId_semilla(semillaForm.getId());
			RastreoDAO.modificarRastreo(c, false, insertarRastreoForm, idCrawler);
			// Disable if is not activa or is eliminada
			if (!semillaForm.isActiva() || semillaForm.isEliminar()) {
				RastreoDAO.enableDisableCrawler(c, idCrawler, false);
			} else {
				RastreoDAO.enableDisableCrawler(c, idCrawler, true);
			}
		}
	}

	/**
	 * Gets the cartridge from executed observatory id.
	 *
	 * @param c             the c
	 * @param observatoryId the observatory id
	 * @return the cartridge from executed observatory id
	 * @throws Exception the SQL exception
	 */
	public static long getCartridgeFromExecutedObservatoryId(Connection c, final Long observatoryId) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT id_cartucho FROM rastreos_realizados WHERE id = ?")) {
			ps.setLong(1, observatoryId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id_cartucho");
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the subsequent observatory execution ids.
	 *
	 * @param c             the c
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param cartridgeId   the cartridge id
	 * @return the subsequent observatory execution ids
	 * @throws Exception the SQL exception
	 */
	public static List<Long> getSubsequentObservatoryExecutionIds(Connection c, Long observatoryId, Long executionId, Long cartridgeId) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<Long> subsequentExecutionIds = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement(
				"SELECT id FROM observatorios_realizados WHERE id_observatorio = ? AND " + "id_cartucho = ? AND fecha > (SELECT fecha FROM observatorios_realizados WHERE id = ? )")) {
			ps.setLong(1, observatoryId);
			ps.setLong(2, cartridgeId);
			ps.setLong(3, executionId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					subsequentExecutionIds.add(rs.getLong("id"));
				}
			}
			return subsequentExecutionIds;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update observatory status.
	 *
	 * @param c          the c
	 * @param executedId the executed id
	 * @param estado     the estado
	 * @throws Exception the SQL exception
	 */
	public static void updateObservatoryStatus(Connection c, Long executedId, int estado) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("UPDATE observatorios_realizados SET estado = ? WHERE id = ?")) {
			ps.setInt(1, estado);
			ps.setLong(2, executedId);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error al modificar el estado de la ejecución con id: " + executedId + " del observatorio.", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the observatory categories.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @return the observatory categories
	 * @throws Exception the SQL exception
	 */
	public static List<CategoriaForm> getObservatoryCategories(Connection c, Long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<CategoriaForm> observatoryCategories = new ArrayList<>();
		try (PreparedStatement ps = c
				.prepareStatement("SELECT * FROM observatorio_categoria oc " + "JOIN categorias_lista cl ON (cl.id_categoria = oc.id_categoria) " + "WHERE id_observatorio = ? ORDER BY cl.nombre")) {
			ps.setLong(1, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString("oc.id_categoria"));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					observatoryCategories.add(categoriaForm);
				}
			}
			return observatoryCategories;
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the execution observatory categories.
	 *
	 * @param c                      the c
	 * @param idExecutionObservatory the id execution observatory
	 * @return the execution observatory categories
	 * @throws Exception the SQL exception
	 */
	public static List<CategoriaForm> getExecutionObservatoryCategories(Connection c, final Long idExecutionObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<CategoriaForm> observatoryCategories = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT cl.nombre, cl.id_categoria, cl.orden FROM rastreos_realizados rr " + "JOIN lista l ON (l.id_lista = rr.id_lista) "
				+ "JOIN observatorio_categoria oc ON (l.id_categoria = oc.id_categoria) " + "JOIN categorias_lista cl ON (oc.id_categoria = cl.id_categoria) "
				+ "WHERE rr.id_obs_realizado = ? GROUP BY cl.id_categoria ORDER BY cl.orden, cl.nombre;")) {
			ps.setLong(1, idExecutionObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString("cl.id_categoria"));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					observatoryCategories.add(categoriaForm);
				}
			}
			return observatoryCategories;
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the execution observatory categories ambit.
	 *
	 * @param c                      the c
	 * @param idExecutionObservatory the id execution observatory
	 * @param idAmbit                the id ambit
	 * @return the execution observatory categories ambit
	 * @throws Exception the SQL exception
	 */
	public static List<CategoriaForm> getExecutionObservatoryPrinmayCategoriesAmbit(Connection c, final Long idExecutionObservatory, final Long idAmbit) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<CategoriaForm> observatoryCategories = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT cl.nombre, cl.id_categoria, cl.orden FROM rastreos_realizados rr " + "JOIN lista l ON (l.id_lista = rr.id_lista) "
				+ "JOIN observatorio_categoria oc ON (l.id_categoria = oc.id_categoria) " + "JOIN categorias_lista cl ON (oc.id_categoria = cl.id_categoria) "
				+ " JOIN rastreo r ON r.id_rastreo = rr.id_rastreo JOIN observatorio o ON r.id_observatorio=o.id_observatorio JOIN ambitos_lista al ON al.id_ambito = o.id_ambito "
				+ " WHERE rr.id_obs_realizado = ?  AND o.id_ambito = ? AND cl.principal = 1 GROUP BY cl.id_categoria ORDER BY cl.orden, cl.nombre;")) {
			ps.setLong(1, idExecutionObservatory);
			ps.setLong(2, idAmbit);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final CategoriaForm categoriaForm = new CategoriaForm();
					categoriaForm.setId(rs.getString("cl.id_categoria"));
					categoriaForm.setName(rs.getString("cl.nombre"));
					categoriaForm.setOrden(rs.getInt("cl.orden"));
					observatoryCategories.add(categoriaForm);
				}
			}
			return observatoryCategories;
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Save methodology.
	 *
	 * @param c           the c
	 * @param idExecution the id execution
	 * @param methodology the methodology
	 * @throws Exception the SQL exception
	 */
	public static void saveMethodology(Connection c, Long idExecution, String methodology) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("INSERT INTO observatorio_metodologia (id_obs_realizado, metodologia) VALUES (?,?)")) {
			ps.setLong(1, idExecution);
			ps.setString(2, methodology);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the methodology.
	 *
	 * @param c           the c
	 * @param idExecution the id execution
	 * @return the methodology
	 * @throws Exception the SQL exception
	 */
	public static String getMethodology(Connection c, Long idExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio_metodologia WHERE id_obs_realizado = ?")) {
			ps.setLong(1, idExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("metodologia");
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the all observatory types.
	 *
	 * @param c the c
	 * @return the all observatory types
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatoryTypeForm> getAllObservatoryTypes(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<ObservatoryTypeForm> typeList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM observatorio_tipo")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ObservatoryTypeForm type = new ObservatoryTypeForm();
					type.setId(rs.getLong("id_tipo"));
					type.setName(rs.getString("name"));
					typeList.add(type);
				}
			}
			return typeList;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar la lista de roles", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the all ambitos.
	 *
	 * @param c the c
	 * @return the all ambitos
	 * @throws Exception the SQL exception
	 */
	public static List<AmbitoForm> getAllAmbitos(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<AmbitoForm> typeList = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SELECT * FROM ambitos_lista")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					AmbitoForm type = new AmbitoForm();
					type.setId(rs.getString("id_ambito"));
					type.setName(rs.getString("nombre"));
					typeList.add(type);
				}
			}
			return typeList;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar la lista de roles", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Dada una ejecución de un observatorio obtiene el identificador de la ejecución inmediatamente previa del mismo observatorio.
	 *
	 * @param c                      Conexión a la base de datos
	 * @param idObservatoryExecution Identificador de la iteración (ejecución) de un observatorio
	 * @return El identificador de la iteración anterior de ese observatorio o -1 si no existe
	 */
	public static Long getPreviousObservatoryExecution(Connection c, final Long idObservatoryExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("SELECT id FROM observatorios_realizados " + "WHERE id_observatorio = ? AND id<? AND estado=0 ORDER BY fecha DESC")) {
			final long idObservatory = getObservatoryFormFromExecution(c, idObservatoryExecution).getId();
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObservatoryExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
			}
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return -1L;
	}

	/**
	 * Adds the seed observatory.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param idExObs       the id ex obs
	 * @param idSeed        the id seed
	 * @param idCartucho    the id cartucho
	 * @throws Exception the SQL exception
	 */
	public static void addSeedObservatory(Connection c, final Long idObservatory, final Long idExObs, final Long idSeed, final Long idCartucho) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		// Check if exists in rastreo table
		SemillaForm seed = SemillaDAO.getSeedById(c, idSeed);
		ObservatorioForm obs = ObservatorioDAO.getObservatoryForm(c, idObservatory);
		try (PreparedStatement ps = c.prepareStatement("select id_rastreo from rastreo where id_observatorio = ? and semillas=?")) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idSeed);
			try (ResultSet rs = ps.executeQuery()) {
				// Exists
				if (rs.next()) {
					// Insert into rastreos_realizados
					Long id = rs.getLong(1);
					// Insert into cartucho_rastreo id_cartucho
					try (PreparedStatement psCR = c
							.prepareStatement("INSERT INTO cartucho_rastreo(id_cartucho, id_rastreo) VALUES(?,?) ON DUPLICATE KEY UPDATE id_cartucho=id_cartucho, id_rastreo = id_rastreo")) {
						psCR.setLong(1, idCartucho);
						psCR.setLong(2, id);
						psCR.executeUpdate();
						addFullfilledCrawl(c, idExObs, id, idSeed);
					} catch (Exception e) {
						Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
						// Remove prevoius insert
						try (PreparedStatement psCR = c.prepareStatement("DELETE FROM cartucho_rastreo WHERE id_cartucho=? AND id_rastreo = ?")) {
							psCR.setLong(1, idCartucho);
							psCR.setLong(2, id);
							psCR.executeUpdate();
							addFullfilledCrawl(c, idExObs, id, idSeed);
						} catch (Exception e2) {
							Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e2);
						}
					}
				} else {
					// Insert into RASTREO
					PreparedStatement psR = c.prepareStatement(
							"INSERT INTO rastreo (nombre_rastreo, fecha, profundidad, topn, semillas, lista_no_rastreable, lista_rastreable, estado, id_cuenta, pseudoaleatorio, activo, id_language, id_observatorio, automatico, exhaustive, in_directory,id_guideline) "
									+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)",
							Statement.RETURN_GENERATED_KEYS);
					psR.setString(1, obs.getNombre() + "-" + seed.getNombre());
					psR.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
					psR.setInt(3, obs.getProfundidad());
					psR.setLong(4, obs.getAmplitud());
					psR.setLong(5, seed.getId());
					psR.setString(6, null);
					psR.setString(7, null);
					psR.setInt(8, Constants.STATUS_NOT_LAUNCHED);
					psR.setNull(9, Types.BIGINT);
					psR.setBoolean(10, obs.isPseudoAleatorio());
					psR.setBoolean(11, seed.isActiva());
					psR.setLong(12, obs.getLenguaje());
					psR.setLong(13, obs.getId());
					psR.setBoolean(14, true);
					psR.setBoolean(15, false);
					psR.setBoolean(16, seed.isInDirectory());
					psR.setLong(17, obs.getId_guideline());
					psR.executeUpdate();
					try (ResultSet rsR = psR.getGeneratedKeys()) {
						if (rs.next()) {
							// Saved
							Long id = rsR.getLong(1);
							// Insert into cartucho_rastreo id_cartucho
							try (PreparedStatement psCR = c.prepareStatement("INSERT INTO cartucho_rastreo(id_cartucho, id_rastreo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
								psCR.setLong(1, idCartucho);
								psCR.setLong(2, id);
								psCR.executeUpdate();
								try (ResultSet rsCR = psCR.getGeneratedKeys()) {
									if (rsCR.next()) {
										addFullfilledCrawl(c, idExObs, id, idSeed);
									} else {
										Logger.putLog("e: " + psCR, ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR);
									}
								} catch (Exception e) {
									Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
								}
							} catch (Exception e) {
								Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
								// Remove prevoius insert
								try (PreparedStatement psCR = c.prepareStatement("DELETE FROM cartucho_rastreo WHERE id_cartucho=? AND id_rastreo = ?")) {
									psCR.setLong(1, idCartucho);
									psCR.setLong(2, id);
									psCR.executeUpdate();
									addFullfilledCrawl(c, idExObs, id, idSeed);
								} catch (Exception e2) {
									Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e2);
								}
							}
						} else {
							// error??
						}
					} catch (Exception e) {
						Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			}
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Adds the fullfilled crawl.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @param id      the id
	 * @param idSeed  the id seed
	 * @throws Exception the SQL exception
	 * @throws Exception the exception
	 */
	private static void addFullfilledCrawl(Connection c, final Long idExObs, Long id, final Long idSeed) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final PropertiesManager pmgr = new PropertiesManager();
		final DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, String.valueOf(id));
		dcrForm.setId_rastreo(id.intValue());
		dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));
		dcrForm.setIdSemilla(idSeed);
		final int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
		dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList(id, typeDomains, false));
		dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList(id, typeDomains, true));
		dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList(id, Constants.ID_LISTA_NO_RASTREABLE, false));
		dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList(id, Constants.ID_LISTA_RASTREABLE, false));
		dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, id));
		if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
			dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
		}
		final DatosForm userData = LoginDAO.getUserDataByName(c, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"));
		final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, idExObs, Long.valueOf(userData.getId()));
	}

	/**
	 * Gets the obserbatories dates.
	 *
	 * @param c        the c
	 * @param exObsIds the ex obs ids
	 * @return the obserbatories dates
	 * @throws Exception the SQL exception
	 */
	public static List<ObservatorioRealizadoForm> getObserbatoriesDates(Connection c, final String[] exObsIds) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ObservatorioRealizadoForm> list = new ArrayList<>();
		String query = "SELECT a.nombre,obr.fecha FROM observatorios_realizados obr JOIN observatorio o ON obr.id_observatorio=o.id_observatorio JOIN ambitos_lista a ON a.id_ambito=o.id_ambito\n"
				+ "WHERE  1=1 ";
		// Cargamos los rastreos realizados
		if (exObsIds != null && exObsIds.length > 0) {
			query = query + "AND obr.id IN (" + exObsIds[0];
			for (int i = 1; i < exObsIds.length; i++) {
				query = query + "," + exObsIds[i];
			}
			query = query + ")";
		}
		query = query + "ORDER BY o.id_ambito ASC";
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ObservatorioRealizadoForm obs = new ObservatorioRealizadoForm();
					obs.setAmbito(rs.getString("a.nombre"));
					obs.setFechaStr(df.format(rs.getTimestamp("fecha")));
					list.add(obs);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getFulfilledObservatory", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return list;
	}

	/**
	 * Load extra configuration.
	 *
	 * @param c the c
	 * @return the list
	 * @throws Exception the SQL exception
	 */
	public static List<ExtraConfigurationForm> loadExtraConfiguration(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ExtraConfigurationForm> extraConfig = new ArrayList<>();
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		final String query = "SELECT `id` AS C_ID,`name` AS N_ID, `key` AS K_ID,`value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` <> 'autorelaunch' ORDER BY `id`";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ExtraConfigurationForm config = new ExtraConfigurationForm();
					config.setId(rs.getLong("C_ID"));
					config.setKey(rs.getString("K_ID"));
					config.setValue(rs.getString("V_ID"));
					config.setName(messageResources.getMessage(rs.getString("N_ID")));
					extraConfig.add(config);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getFulfilledObservatory", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return extraConfig;
	}

	/**
	 * Gets the extra configuration.
	 *
	 * @param c   the c
	 * @param key the key
	 * @return the extra configuration
	 * @throws Exception the SQL exception
	 */
	public static List<ExtraConfigurationForm> getExtraConfiguration(Connection c, final String key) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ExtraConfigurationForm> extraConfig = new ArrayList<>();
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		final String query = "SELECT `id` AS C_ID,`name` AS N_ID, `key` AS K_ID,`value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = '" + key + "' ORDER BY `id`";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ExtraConfigurationForm config = new ExtraConfigurationForm();
					config.setId(rs.getLong("C_ID"));
					config.setKey(rs.getString("K_ID"));
					config.setValue(rs.getString("V_ID"));
					config.setName(messageResources.getMessage(rs.getString("N_ID")));
					extraConfig.add(config);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getFulfilledObservatory", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return extraConfig;
	}

	/**
	 * Gets the timout.
	 *
	 * @param c the c
	 * @return the timout
	 * @throws Exception the SQL exception
	 */
	public static int getTimeoutFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int timeout = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'timeout'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						timeout = Integer.parseInt(value);
						return timeout;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getTimeoutFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return timeout;
	}

	/**
	 * Gets the file expiration from config.
	 *
	 * @param c the c
	 * @return the file expiration from config
	 * @throws Exception the SQL exception
	 */
	public static int getFileExpirationFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int timeout = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'files_expiration'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						timeout = Integer.parseInt(value);
						return timeout;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getTimeoutFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return timeout;
	}

	/**
	 * Gets the depth from config.
	 *
	 * @param c the c
	 * @return the depth from config
	 * @throws Exception the SQL exception
	 */
	public static int getDepthFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int timeout = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'depth'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						timeout = Integer.parseInt(value);
						return timeout;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getDepthFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return timeout;
	}

	/**
	 * Gets the depth from width.
	 *
	 * @param c the c
	 * @return the depth from width
	 * @throws Exception the SQL exception
	 */
	public static int getWidthFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int timeout = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'width'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						timeout = Integer.parseInt(value);
						return timeout;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getWidthFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return timeout;
	}

	/**
	 * Gets the depth from width.
	 *
	 * @param c the c
	 * @return the depth from width
	 * @throws Exception the SQL exception
	 */
	public static int getTresholdFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int timeout = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'umbral'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						timeout = Integer.parseInt(value);
						return timeout;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getTresholdFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return timeout;
	}

	/**
	 * Gets the mapping from config.
	 *
	 * @param c the c
	 * @return the mapping from config
	 * @throws Exception the SQL exception
	 */
	public static String getMappingFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		String mapping = "";
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'file_mapping'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					try {
						mapping = rs.getString("V_ID");
						return mapping;
					} catch (Exception e) {
						return "";
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getTresholdFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return mapping;
	}

	/**
	 * Save extra configurarion.
	 *
	 * @param c           the c
	 * @param extraConfig the extra config
	 * @throws Exception the SQL exception
	 */
	public static void saveExtraConfiguration(Connection c, final List<ExtraConfigurationForm> extraConfig) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		if (extraConfig != null && !extraConfig.isEmpty()) {
			for (ExtraConfigurationForm config : extraConfig) {
				try (PreparedStatement ps = c.prepareStatement("UPDATE observatorio_extra_configuration SET `value` = ? WHERE `key` = ?")) {
					ps.setString(1, config.getValue());
					ps.setString(2, config.getKey());
					ps.executeUpdate();
				} catch (SQLException e) {
					Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
					throw e;
				}
			}
		}
	}

	/**
	 * Save extra configuration.
	 *
	 * @param c     the c
	 * @param key   the key
	 * @param value the value
	 * @throws Exception the SQL exception
	 */
	public static void saveExtraConfiguration(Connection c, final String key, final String value) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("UPDATE observatorio_extra_configuration SET `value` = ? WHERE `key` = ?")) {
			ps.setString(1, value);
			ps.setString(2, key);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Excepcion: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the autorelaunch from config.
	 *
	 * @param c the c
	 * @return the autorelaunch from config
	 * @throws Exception the SQL exception
	 */
	public static int getAutorelaunchFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int autorelaunch = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'autorelaunch'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						autorelaunch = Integer.parseInt(value);
						return autorelaunch;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getAutorelaunchFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return autorelaunch;
	}

	/**
	 * Gets the first classification threshold from config.
	 *
	 * @param c the c
	 * @return the first classification threshold from config.
	 * @throws Exception the SQL exception
	 */
	public static double getFirstClassificationThresholdFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		double firstThreshold = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'firstclassthreshold'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						firstThreshold = Double.parseDouble(value);
						return firstThreshold;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getFirstClassificationThresholdFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return firstThreshold;
	}

	/**
	 * Gets the second classification threshold from config.
	 *
	 * @param c the c
	 * @return the second classification threshold from config.
	 * @throws Exception the SQL exception
	 */
	public static double getSecondClassificationThresholdFromConfig(Connection c) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		double secondThreshold = 0;
		final String query = "SELECT `value` AS V_ID FROM `observatorio_extra_configuration` WHERE `key` = 'secondclassthreshold'";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String value = rs.getString("V_ID");
					try {
						secondThreshold = Double.parseDouble(value);
						return secondThreshold;
					} catch (Exception e) {
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getSecondClassificationThresholdFromConfig", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return secondThreshold;
	}

	/**
	 * Gets the num crawls.
	 *
	 * @param c             the c
	 * @param idObservatory the id observatory
	 * @param idSeed        the id seed
	 * @return the num crawls
	 * @throws Exception the SQL exception
	 */
	public static int getNumCrawls(Connection c, final Long idObservatory, final Long idSeed) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int numCrawls = 0;
		// Count URL crawled
		String numCrawlQuery = "SELECT count(ta.cod_url) as numCrawls  "
				+ "FROM tanalisis ta, rastreos_realizados rr, rastreo r, lista l WHERE ta.cod_rastreo = rr.id  and rr.id_rastreo = r.id_rastreo and r.semillas = l.id_lista  "
				+ "and ta.cod_rastreo in (select rr2.id from rastreos_realizados rr2 where rr2.id_obs_realizado=?) and rr.id_lista = ?";
		PreparedStatement psCrawls = c.prepareStatement(numCrawlQuery);
		psCrawls.setLong(1, idObservatory);
		psCrawls.setLong(2, idSeed);
		ResultSet rsCrawls = null;
		try {
			rsCrawls = psCrawls.executeQuery();
			if (rsCrawls.next()) {
				numCrawls = rsCrawls.getInt("numCrawls");
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(psCrawls, rsCrawls);
		}
		return numCrawls;
	}

	/**
	 * Save config.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @param exObsIds       the ex obs ids
	 * @param comparision    the comparision
	 * @throws Exception                    the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void saveConfig(Connection c, final Long idObsExecution, final String[] exObsIds, final List<ComparisionForm> comparision) throws Exception, UnsupportedEncodingException {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		try {
//			// Delete existing
			ps = c.prepareStatement("DELETE FROM observatorio_send_configuration_comparision WHERE id_observatory_execution = " + idObsExecution);
			ps.executeUpdate();
			DAOUtils.closeQueries(ps, null);
//			ps = c.prepareStatement("DELETE FROM observatorio_send_configuration WHERE id_observatory_execution = " + idObsExecution);
//			ps.executeUpdate();
//			DAOUtils.closeQueries(ps, null);
			ps = c.prepareStatement("SELECT id FROM observatorio_send_configuration WHERE id_observatory_execution = " + idObsExecution);
			Integer id = null;
			ResultSet rsE = null;
			try {
				rsE = ps.executeQuery();
				if (rsE.next()) {
					id = rsE.getInt("id");
				}
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			DAOUtils.closeQueries(ps, null);
			if (id != null) {
				// update existing
				ps = c.prepareStatement("UPDATE observatorio_send_configuration SET id_observatory_execution =?, ids_observatory_execution_evolution =? WHERE id = ?");
				ps.setLong(1, idObsExecution);
				ps.setString(2, String.join(",", exObsIds));
				ps.setInt(3, id);
				ps.executeUpdate();
				if (comparision != null && !comparision.isEmpty()) {
					for (ComparisionForm cmp : comparision) {
						ps = c.prepareStatement("INSERT INTO observatorio_send_configuration_comparision(id_observatory_execution, id_tag, date_first, date_previous) " + "VALUES (?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
						ps.setLong(1, idObsExecution);
						ps.setLong(2, cmp.getIdTag());
						ps.setString(3, cmp.getFirst());
						ps.setString(4, cmp.getPrevious());
						ps.executeUpdate();
						DAOUtils.closeQueries(ps, null);
					}
				}
			} else {
				// Insert new
				ps = c.prepareStatement("INSERT INTO observatorio_send_configuration(id_observatory_execution, ids_observatory_execution_evolution) " + "VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, idObsExecution);
				ps.setString(2, String.join(",", exObsIds));
				ps.executeUpdate();
				// Insert new
				ps = c.prepareStatement("INSERT INTO observatorio_send_configuration(id_observatory_execution, ids_observatory_execution_evolution) " + "VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, idObsExecution);
				ps.setString(2, String.join(",", exObsIds));
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						if (comparision != null && !comparision.isEmpty()) {
							for (ComparisionForm cmp : comparision) {
								ps = c.prepareStatement("INSERT INTO observatorio_send_configuration_comparision(id_observatory_execution, id_tag, date_first, date_previous) " + "VALUES (?,?,?,?)",
										Statement.RETURN_GENERATED_KEYS);
								ps.setLong(1, idObsExecution);
								ps.setLong(2, cmp.getIdTag());
								ps.setString(3, cmp.getFirst());
								ps.setString(4, cmp.getPrevious());
								ps.executeUpdate();
								DAOUtils.closeQueries(ps, null);
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Save config step 2.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @param subject        the subject
	 * @param cco            the cco
	 * @throws Exception                    the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void saveConfigStep2(Connection c, final Long idObsExecution, final String subject, final String cco, final Long hasCustomTexts) throws Exception, UnsupportedEncodingException {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		try {
			// Insert new
			ps = c.prepareStatement("UPDATE observatorio_send_configuration SET subject = ?, cco = ?, has_custom_texts = ?  WHERE id_observatory_execution = ?");
			ps.setString(1, subject);
			ps.setString(2, cco);
			ps.setLong(3, hasCustomTexts);
			ps.setLong(4, idObsExecution);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL_EXCEPTION: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Gets the config step 2.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @param subject        the subject
	 * @param cco            the cco
	 * @return the config step 2
	 * @throws Exception                    the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static Map<String, String> getConfigStep2(Connection c, final Long idObsExecution) throws Exception, UnsupportedEncodingException {
		c = reOpenConnectionIfIsNecessary(c);
		Map<String, String> config = new HashMap<>();
		PreparedStatement ps = null;
		try {
			// Insert new
			ps = c.prepareStatement("SELECT subject, cco FROM observatorio_send_configuration WHERE id_observatory_execution = ?");
			ps.setLong(1, idObsExecution);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				config.put("emailSubject", rs.getString("subject"));
				config.put("cco", rs.getString("cco"));
			}
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
		return config;
	}

	/**
	 * Gets the comparision config.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @return the comparision config
	 * @throws Exception the SQL exception
	 */
	public static List<ComparisionForm> getComparisionConfig(Connection c, final Long idObsExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<ComparisionForm> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement("SELECT id, id_observatory_execution, id_tag, date_first, date_previous FROM observatorio_send_configuration_comparision WHERE id_observatory_execution = ?");
			ps.setLong(1, idObsExecution);
			rs = ps.executeQuery();
			while (rs.next()) {
				ComparisionForm cmp = new ComparisionForm();
				cmp.setIdTag(rs.getInt("id_tag"));
				cmp.setFirst(rs.getString("date_first"));
				cmp.setPrevious(rs.getString("date_previous"));
				EtiquetaForm tag = EtiquetaDAO.getById(c, cmp.getIdTag());
				if (tag != null) {
					cmp.setTagName(tag.getName());
				}
				list.add(cmp);
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return list;
	}

	/**
	 * Gets the ex obs ids config.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @return the ex obs ids config
	 * @throws Exception the SQL exception
	 */
	public static String[] getExObsIdsConfig(Connection c, Long idObsExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(
					"SELECT id, id_observatory_execution, ids_observatory_execution_evolution, has_custom_texts FROM observatorio_send_configuration WHERE id_observatory_execution = ?");
			ps.setLong(1, idObsExecution);
			rs = ps.executeQuery();
			while (rs.next()) {
				String exObsIds = rs.getString("ids_observatory_execution_evolution");
				if (exObsIds != null && !org.apache.commons.lang3.StringUtils.isEmpty(exObsIds)) {
					return exObsIds.split(",");
				}
				return null;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return null;
	}

	/**
	 * Gets the dependencies by id ex obs.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @return the dependencies by id ex obs
	 */
	public static List<DependenciaForm> getDependenciesByIdExObs(Connection c, final Long idObsExecution) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<DependenciaForm> dependencies = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = c.prepareStatement(
					"SELECT d.id_dependencia, d.nombre, d.emails, d.send_auto, d.official FROM rastreos_realizados rr JOIN lista l ON rr.id_lista=l.id_lista JOIN semilla_dependencia sd ON sd.id_lista=l.id_lista JOIN dependencia d ON d.id_dependencia=sd.id_dependencia WHERE rr.id_obs_realizado = ?");
			ps.setLong(1, idObsExecution);
			rs = ps.executeQuery();
			while (rs.next()) {
				DependenciaForm d = new DependenciaForm();
				d.setId(rs.getLong("d.id_dependencia"));
				d.setName(rs.getString("d.nombre"));
				d.setEmails(formatEmails(rs.getString("d.emails")));
				d.setSendAuto(rs.getBoolean("d.send_auto"));
				d.setOfficial(rs.getBoolean("d.official"));
				dependencies.add(d);
			}
		} catch (SQLException e) {
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return dependencies;
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
