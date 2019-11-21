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
package es.inteco.rastreador2.dao.rastreo;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.dao.EstadoObservatorioDAO;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosForm;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosRealizadosSearchForm;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosSearchForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.rastreo.RastreoEjecutadoForm;
import es.inteco.rastreador2.actionform.rastreo.VerRastreoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.UpdateListDataForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.Rastreo;

/**
 * The Class RastreoDAO.
 */
public final class RastreoDAO {
	/**
	 * Instantiates a new rastreo DAO.
	 */
	private RastreoDAO() {
	}

	/**
	 * Gets the execution observatory crawler ids match tags.
	 *
	 * @param c                      the c
	 * @param idObservatoryExecution the id observatory execution
	 * @param tagsToFiler            the tags to filer
	 * @return the execution observatory crawler ids match tags
	 * @throws Exception the exception
	 */
	public static List<Long> getExecutionObservatoryCrawlerIdsMatchTags(Connection c, Long idObservatoryExecution, String[] tagsToFiler) throws Exception {
		final List<Long> executionObservatoryCrawlersIds = new ArrayList<>();
		String query = "SELECT id FROM rastreos_realizados rr JOIN rastreo r ON (r.id_rastreo = rr.id_rastreo) JOIN lista l ON (l.id_lista = r.semillas) JOIN etiquetas_lista el ON l.id_lista=el.id_lista WHERE id_obs_realizado = ? ";
		if (tagsToFiler != null && tagsToFiler.length > 0) {
			query = query + " AND ( 1=1 ";
			for (int i = 0; i < tagsToFiler.length; i++) {
				query = query + " OR el.id_etiqueta= ?";
			}
			query = query + ")";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatoryExecution);
			if (tagsToFiler != null && tagsToFiler.length > 0) {
				for (int i = 0; i < tagsToFiler.length; i++) {
					ps.setLong(i + 2, Long.parseLong(tagsToFiler[i]));
				}
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executionObservatoryCrawlersIds.add(rs.getLong(1));
				}
				return executionObservatoryCrawlersIds;
			}
		} catch (Exception e) {
			Logger.putLog("Error en getExecutionObservatoryCrawlerIds", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the execution observatory crawler ids.
	 *
	 * @param c                      the c
	 * @param idObservatoryExecution the id observatory execution
	 * @param idCategory             the id category
	 * @return the execution observatory crawler ids
	 * @throws Exception the exception
	 */
	public static List<Long> getExecutionObservatoryCrawlerIds(Connection c, Long idObservatoryExecution, long idCategory) throws Exception {
		final List<Long> executionObservatoryCrawlersIds = new ArrayList<>();
		String query = "SELECT id FROM rastreos_realizados rr " + "JOIN rastreo r ON (r.id_rastreo = rr.id_rastreo) " + "JOIN lista l ON (l.id_lista = r.semillas) " + "WHERE id_obs_realizado = ?";
		if (idCategory != 0) {
			query = query + " AND l.id_categoria = ? ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatoryExecution);
			if (idCategory != 0) {
				ps.setLong(2, idCategory);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executionObservatoryCrawlersIds.add(rs.getLong(1));
				}
				return executionObservatoryCrawlersIds;
			}
		} catch (Exception e) {
			Logger.putLog("Error en getExecutionObservatoryCrawlerIds", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the execution observatory crawler ids complexity.
	 *
	 * @param c                      the c
	 * @param idObservatoryExecution the id observatory execution
	 * @param idComplexity           the id complexity
	 * @return the execution observatory crawler ids complexity
	 * @throws Exception the exception
	 */
	public static List<Long> getExecutionObservatoryCrawlerIdsComplexity(Connection c, Long idObservatoryExecution, long idComplexity) throws Exception {
		final List<Long> executionObservatoryCrawlersIds = new ArrayList<>();
		String query = "SELECT id FROM rastreos_realizados rr " + "JOIN rastreo r ON (r.id_rastreo = rr.id_rastreo) " + "JOIN lista l ON (l.id_lista = r.semillas) " + "WHERE id_obs_realizado = ?";
		if (idComplexity != 0) {
			query = query + " AND l.id_complejidad = ? ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatoryExecution);
			if (idComplexity != 0) {
				ps.setLong(2, idComplexity);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executionObservatoryCrawlersIds.add(rs.getLong(1));
				}
				return executionObservatoryCrawlersIds;
			}
		} catch (Exception e) {
			Logger.putLog("Error en getExecutionObservatoryCrawlerIdsComplexity", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the id rastreo realizado from id rastreo and id observatory execution.
	 *
	 * @param c                      the c
	 * @param idRastreo              the id rastreo
	 * @param idObservatoryExecution the id observatory execution
	 * @return the id rastreo realizado from id rastreo and id observatory execution
	 * @throws Exception the exception
	 */
	public static long getIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(Connection c, long idRastreo, long idObservatoryExecution) throws Exception {
		final String query = "SELECT id FROM rastreos_realizados rr " + "WHERE id_rastreo = ? AND " + "id_obs_realizado = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idRastreo);
			ps.setLong(2, idObservatoryExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en getIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Crawler can be throw.
	 *
	 * @param c          the c
	 * @param userLogin  the user login
	 * @param cartuchoID the cartucho ID
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean crawlerCanBeThrow(Connection c, String userLogin, int cartuchoID) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM usuario u JOIN usuario_cartucho uc ON (u.id_usuario = uc.id_usuario) " + "WHERE u.usuario = ? AND uc.id_cartucho = ?")) {
			ps.setString(1, userLogin);
			ps.setLong(2, cartuchoID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			Logger.putLog("Error en crawlerCanBeThrow", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Crawler to user.
	 *
	 * @param c          the c
	 * @param idCrawling the id crawling
	 * @param userLogin  the user login
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean crawlerToUser(Connection c, Long idCrawling, String userLogin) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM usuario u JOIN usuario_cartucho uc ON (u.id_usuario = uc.id_usuario) "
				+ "JOIN cartucho_rastreo cr ON (uc.id_cartucho = cr.id_cartucho) WHERE u.usuario = ? AND cr.id_rastreo = ?")) {
			ps.setString(1, userLogin);
			ps.setLong(2, idCrawling);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en crawlerToUser", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Crawler to client account.
	 *
	 * @param c           the c
	 * @param idCrawling  the id crawling
	 * @param clientLogin the client login
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean crawlerToClientAccount(Connection c, Long idCrawling, String clientLogin) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM usuario u JOIN cuenta_cliente_usuario ccu ON (u.id_usuario = ccu.id_usuario) "
				+ "JOIN rastreo r ON (r.id_cuenta = ccu.id_cuenta) " + "WHERE u.usuario = ? AND r.id_rastreo = ?")) {
			ps.setString(1, clientLogin);
			ps.setLong(2, idCrawling);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en crawlerToClientAccount", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Count rastreos realizados.
	 *
	 * @param c          the c
	 * @param idCrawling the id crawling
	 * @param searchForm the search form
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int countRastreosRealizados(Connection c, Long idCrawling, CargarRastreosRealizadosSearchForm searchForm) throws Exception {
		int count = 1;
		String query = "SELECT COUNT(*) FROM rastreos_realizados rr " + "JOIN usuario u ON (u.id_usuario = rr.id_usuario) WHERE id_rastreo = ?";
		if (searchForm != null) {
			if (searchForm.getInitial_date() != null && !searchForm.getInitial_date().isEmpty()) {
				query += " AND rr.fecha >= ?";
			}
			if (searchForm.getFinal_date() != null && !searchForm.getFinal_date().isEmpty()) {
				query += " AND rr.fecha <= ? ";
			}
			if (searchForm.getCartridge() != null && !searchForm.getCartridge().isEmpty()) {
				query += " AND rr.id_cartucho = ? ";
			}
			if (searchForm.getSeed() != null && !searchForm.getSeed().isEmpty()) {
				query += " AND rr.id_lista = ? ";
			}
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(count++, idCrawling);
			if (searchForm != null) {
				if (searchForm.getInitial_date() != null && !searchForm.getInitial_date().isEmpty()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date date = sdf.parse(searchForm.getInitial_date());
					ps.setTimestamp(count++, new Timestamp(date.getTime()));
				}
				if (searchForm.getFinal_date() != null && !searchForm.getFinal_date().isEmpty()) {
					searchForm.setFinal_date(searchForm.getFinal_date());
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
					Date date = sdf.parse(searchForm.getFinal_date() + " 23:59:59");
					ps.setTimestamp(count++, new Timestamp(date.getTime()));
				}
				if (searchForm.getCartridge() != null && !searchForm.getCartridge().isEmpty()) {
					ps.setLong(count++, Long.parseLong(searchForm.getCartridge()));
				}
				if (searchForm.getSeed() != null && !searchForm.getSeed().isEmpty()) {
					ps.setLong(count, Long.parseLong(searchForm.getSeed()));
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
			Logger.putLog("Error en countRastreosRealizados", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count rastreo.
	 *
	 * @param c          the c
	 * @param user       the user
	 * @param searchForm the search form
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int countRastreo(Connection c, String user, CargarRastreosSearchForm searchForm) throws Exception {
		String query = "SELECT COUNT(*) FROM rastreo r " + "JOIN cartucho_rastreo cr ON r.Id_rastreo = cr.Id_rastreo " + "JOIN cartucho c ON cr.Id_cartucho = c.Id_cartucho "
				+ "JOIN usuario_cartucho uc ON uc.Id_cartucho = c.Id_cartucho " + "JOIN usuario u ON u.Id_usuario = uc.Id_usuario WHERE u.usuario = ? " + "AND id_observatorio IS NULL ";
		if (StringUtils.isNotEmpty(searchForm.getName())) {
			query += " AND r.nombre_rastreo like ? ";
		}
		if (StringUtils.isNotEmpty(searchForm.getCartridge())) {
			query += " AND cr.id_cartucho = ? ";
		}
		if (StringUtils.isNotEmpty(searchForm.getActive())) {
			query += " AND activo = ? ";
		}
		int paramCount = 1;
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(paramCount++, user);
			if (StringUtils.isNotEmpty(searchForm.getName())) {
				ps.setString(paramCount++, "%" + searchForm.getName() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getCartridge())) {
				ps.setLong(paramCount++, Long.parseLong(searchForm.getCartridge()));
			}
			if (StringUtils.isNotEmpty(searchForm.getActive())) {
				ps.setLong(paramCount, Long.parseLong(searchForm.getActive()));
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en countRastreo", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Devuelve el listado de rastreos a los que puede acceder un usuario.
	 *
	 * @param c          the c
	 * @param user       the user
	 * @param searchForm the search form
	 * @param pagina     the pagina
	 * @return the load crawling form
	 * @throws SQLException the SQL exception
	 */
	public static CargarRastreosForm getLoadCrawlingForm(Connection c, String user, CargarRastreosSearchForm searchForm, int pagina) throws SQLException {
		final CargarRastreosForm cargarRastreosForm = new CargarRastreosForm();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		int paramCount = 1;
		String query = "SELECT * FROM rastreo r JOIN cartucho_rastreo cr ON r.Id_rastreo = cr.Id_rastreo " + "JOIN cartucho c ON cr.Id_cartucho = c.Id_cartucho "
				+ "JOIN usuario_cartucho uc ON uc.Id_cartucho = c.Id_cartucho " + "JOIN usuario u ON u.Id_usuario = uc.Id_usuario " + "WHERE u.usuario = ? AND id_observatorio IS NULL ";
		if (StringUtils.isNotEmpty(searchForm.getName())) {
			query += " AND r.nombre_rastreo like ? ";
		}
		if (StringUtils.isNotEmpty(searchForm.getCartridge())) {
			query += " AND cr.id_cartucho = ? ";
		}
		if (StringUtils.isNotEmpty(searchForm.getActive())) {
			query += " AND activo = ? ";
		}
		query += "ORDER BY r.fecha_lanzado DESC LIMIT ? OFFSET ?";
		try (PreparedStatement pstmt = c.prepareStatement(query)) {
			pstmt.setString(paramCount++, user);
			if (StringUtils.isNotEmpty(searchForm.getName())) {
				pstmt.setString(paramCount++, "%" + searchForm.getName() + "%");
			}
			if (StringUtils.isNotEmpty(searchForm.getCartridge())) {
				pstmt.setLong(paramCount++, Long.parseLong(searchForm.getCartridge()));
			}
			if (StringUtils.isNotEmpty(searchForm.getActive())) {
				pstmt.setLong(paramCount++, Long.parseLong(searchForm.getActive()));
			}
			pstmt.setInt(paramCount++, pagSize);
			pstmt.setInt(paramCount, resultFrom);
			try (ResultSet rst = pstmt.executeQuery()) {
				int numRastreos = 0;
				boolean firstIteration = true;
				final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
				final List<Rastreo> rastreos = new ArrayList<>();
				while (rst.next()) {
					if (firstIteration) {
						cargarRastreosForm.setMaxrastreos(rst.getInt("numrastreos"));
						cargarRastreosForm.setCartucho(rst.getString("aplicacion"));
						firstIteration = false;
					}
					numRastreos++;
					Rastreo r = new Rastreo();
					int idRastreo = rst.getInt("id_rastreo");
					// Código del rastreo
					String nombreRastreo = rst.getString("nombre_rastreo");
					if (nombreRastreo.contains("-")) {
						nombreRastreo = nombreRastreo.substring(0, nombreRastreo.indexOf('-'));
					}
					if (nombreRastreo.length() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string"))) {
						r.setCodigo(nombreRastreo.substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string"))) + "...");
						r.setCodigoTitle(nombreRastreo);
					} else {
						r.setCodigo(nombreRastreo);
					}
					r.setActivo(rst.getLong("activo"));
					r.setIdRastreo(String.valueOf(idRastreo));
					r.setPseudoAleatorio(String.valueOf(rst.getBoolean("pseudoaleatorio")));
					r.setProfundidad(rst.getInt("profundidad"));
					// Fecha del rastreo
					Date date = rst.getDate("fecha");
					if (date != null) {
						r.setFecha(df.format(date));
					}
					r.setCartucho(rst.getString("aplicacion"));
					// Obtenemos el estado del rastreo
					// 1: NO LANZADO
					// 2: LANZADO
					// 3: PARADO
					// 4: TERMINADO
					// int est = -1;
					r.setEstado(rst.getInt("estado"));
					r.setEstadoTexto("rastreo.estado." + r.getEstado());
					r.setIdCuenta(rst.getLong("id_cuenta"));
					rastreos.add(r);
				}
				cargarRastreosForm.setVr(rastreos);
				cargarRastreosForm.setNum_rastreos(numRastreos);
			}
		} catch (SQLException e) {
			Logger.putLog("Error al obtener los datos de la lista de rastreos", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return cargarRastreosForm;
	}

	/**
	 * Exist account from crawler.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean existAccountFromCrawler(Connection c, long idRastreo) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_cuenta FROM rastreo WHERE id_rastreo = ?")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id_cuenta") != 0;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Gets the id LR from rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @return the id LR from rastreo
	 * @throws Exception the exception
	 */
	public static long getIdLRFromRastreo(Connection c, long idRastreo) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT lista_rastreable FROM rastreo WHERE id_rastreo = ? ")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("lista_rastreable");
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Gets the id LNR from rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @return the id LNR from rastreo
	 * @throws Exception the exception
	 */
	public static long getIdLNRFromRastreo(Connection c, long idRastreo) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT lista_no_rastreable FROM rastreo WHERE id_rastreo = ? ")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("lista_no_rastreable");
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return 0;
	}

	/**
	 * Borrar rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @throws SQLException the SQL exception
	 */
	public static void borrarRastreo(Connection c, long idRastreo) throws SQLException {
		final boolean isAutoCommit = c.getAutoCommit();
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM rastreo WHERE id_rastreo = ?")) {
			c.setAutoCommit(false);
			final boolean existAccount = existAccountFromCrawler(c, idRastreo);
			final long idListaRastreable = getIdLRFromRastreo(c, idRastreo);
			final long idListaNoRastreable = getIdLNRFromRastreo(c, idRastreo);
			final List<Long> executedCrawlingIdsList = getExecutedCrawlerIds(c, idRastreo);
			ps.setLong(1, idRastreo);
			ps.executeUpdate();
			try (PreparedStatement deleteListaStatement = c.prepareStatement("DELETE FROM lista WHERE id_lista = ?")) {
				if (!existAccount) {
					if (idListaRastreable != 0) {
						deleteListaStatement.setLong(1, idListaRastreable);
						deleteListaStatement.executeUpdate();
					}
					if (idListaNoRastreable != 0) {
						deleteListaStatement.setLong(1, idListaNoRastreable);
						deleteListaStatement.executeUpdate();
					}
				}
				if (executedCrawlingIdsList != null && !executedCrawlingIdsList.isEmpty()) {
					deleteAnalyse(c, executedCrawlingIdsList);
				}
			} catch (SQLException e) {
				c.rollback();
				c.setAutoCommit(isAutoCommit);
				throw e;
			}
			c.commit();
			c.setAutoCommit(isAutoCommit);
		} catch (Exception e) {
			c.rollback();
			c.setAutoCommit(isAutoCommit);
		}
	}

	/**
	 * Borrar rastreo realizado.
	 *
	 * @param c                  the c
	 * @param idRastreoRealizado the id rastreo realizado
	 * @throws Exception the exception
	 */
	public static void borrarRastreoRealizado(final Connection c, final long idRastreoRealizado) throws Exception {
		final boolean isAutoCommit = c.getAutoCommit();
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM rastreos_realizados WHERE id = ?")) {
			c.setAutoCommit(false);
			ps.setLong(1, idRastreoRealizado);
			ps.executeUpdate();
			final List<Long> executedCrawlingIdsList = new ArrayList<>();
			executedCrawlingIdsList.add(idRastreoRealizado);
			deleteAnalyse(c, executedCrawlingIdsList);
			if (isAutoCommit) {
				c.commit();
			}
			c.setAutoCommit(isAutoCommit);
		} catch (SQLException e) {
			c.rollback();
			c.setAutoCommit(isAutoCommit);
			Logger.putLog("Error al borrarRastreoRealizado", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the executed crawler ids.
	 *
	 * @param connR     the conn R
	 * @param idRastreo the id rastreo
	 * @return the executed crawler ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getExecutedCrawlerIds(Connection connR, long idRastreo) throws SQLException {
		final List<Long> executedCrawlerIds = new ArrayList<>();
		// RECUPERAMOS LOS IDS DE LOS RASTREOS REALIZADOS
		try (PreparedStatement ps = connR.prepareStatement("SELECT rr.id FROM rastreo r " + "JOIN rastreos_realizados rr ON (r.id_rastreo = rr.id_rastreo) " + "WHERE r.id_rastreo = ?")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executedCrawlerIds.add(rs.getLong("id"));
				}
			}
			return executedCrawlerIds;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the executed crawler id.
	 *
	 * @param connR                 the conn R
	 * @param idRastreo             the id rastreo
	 * @param idExecutedObservatory the id executed observatory
	 * @return the executed crawler id
	 * @throws SQLException the SQL exception
	 */
	public static Long getExecutedCrawlerId(Connection connR, long idRastreo, long idExecutedObservatory) throws SQLException {
		try (PreparedStatement ps = connR
				.prepareStatement("SELECT rr.id FROM rastreo r " + "JOIN rastreos_realizados rr ON (r.id_rastreo = rr.id_rastreo) " + "WHERE r.id_rastreo = ? AND rr.id_obs_realizado = ?")) {
			// RECUPERAMOS LOS IDS DE LOS RASTREOS REALIZADOS
			ps.setLong(1, idRastreo);
			ps.setLong(2, idExecutedObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
				return null;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the evolution executed crawler ids.
	 *
	 * @param connR      the conn R
	 * @param idRastreo  the id rastreo
	 * @param idTracking the id tracking
	 * @param idCartucho the id cartucho
	 * @return the evolution executed crawler ids
	 * @throws Exception the exception
	 */
	public static List<Long> getEvolutionExecutedCrawlerIds(Connection connR, long idRastreo, long idTracking, long idCartucho) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		int limit;
		try {
			limit = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "intav.evolution.limit"));
		} catch (NumberFormatException nfe) {
			limit = 4;
		}
		return getEvolutionExecutedCrawlerIds(connR, idRastreo, idTracking, idCartucho, limit);
	}

	/**
	 * Gets the evolution executed crawler ids.
	 *
	 * @param connection         the connection
	 * @param idRastreo          the id rastreo
	 * @param idRastreoRealizado the id rastreo realizado
	 * @param idCartucho         the id cartucho
	 * @param limit              the limit
	 * @return the evolution executed crawler ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getEvolutionExecutedCrawlerIds(final Connection connection, long idRastreo, long idRastreoRealizado, long idCartucho, int limit) throws SQLException {
		final List<Long> executedCrawlerIds = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement("SELECT r.id FROM rastreos_realizados r " + "WHERE id_rastreo = ? AND "
				+ "fecha <= (SELECT fecha FROM rastreos_realizados rr WHERE id = ?) AND " + "id_cartucho = ? " + "ORDER BY fecha DESC LIMIT ?")) {
			ps.setLong(1, idRastreo);
			ps.setLong(2, idRastreoRealizado);
			ps.setLong(3, idCartucho);
			ps.setInt(4, limit);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executedCrawlerIds.add(rs.getLong("id"));
				}
			}
			return executedCrawlerIds;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the executed crawler client accounts ids.
	 *
	 * @param connR           the conn R
	 * @param idClientAccount the id client account
	 * @return the executed crawler client accounts ids
	 * @throws SQLException the SQL exception
	 */
	public static List<Long> getExecutedCrawlerClientAccountsIds(Connection connR, long idClientAccount) throws SQLException {
		final List<Long> executedCrawlerIds = new ArrayList<>();
		try (PreparedStatement ps = connR.prepareStatement("SELECT id FROM rastreos_realizados rr " + "JOIN rastreo r ON (rr.id_rastreo = r.id_rastreo) WHERE r.id_cuenta = ?")) {
			ps.setLong(1, idClientAccount);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					executedCrawlerIds.add(rs.getLong("id"));
				}
			}
			return executedCrawlerIds;
		} catch (SQLException sqle) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, sqle);
			throw sqle;
		}
	}

	/**
	 * Delete analyse.
	 *
	 * @param connection            the connection
	 * @param idsRastreosRealizados the ids rastreos realizados
	 * @throws SQLException the SQL exception
	 */
	public static void deleteAnalyse(Connection connection, List<Long> idsRastreosRealizados) throws SQLException {
		if (idsRastreosRealizados != null && !idsRastreosRealizados.isEmpty()) {
			// ELIMINAMOS LOS ANÁLISIS E INCIDENCIAS DE LOS RASTREOS DEL
			// OBSERVATORIO
			final StringBuilder executionIdStrList = new StringBuilder("(");
			for (int i = 1; i <= idsRastreosRealizados.size(); i++) {
				if (idsRastreosRealizados.size() > i) {
					executionIdStrList.append("?,");
				} else if (idsRastreosRealizados.size() == i) {
					executionIdStrList.append("?)");
				}
			}
			if (connection.getCatalog().contains("intav")) {
				StringBuilder tanalisis = new StringBuilder("DELETE FROM tanalisis WHERE cod_rastreo IN ");
				try (PreparedStatement ps = connection.prepareStatement(tanalisis.append(executionIdStrList).toString())) {
					for (int i = 0; i < idsRastreosRealizados.size(); i++) {
						ps.setLong(i + 1, idsRastreosRealizados.get(i));
					}
					ps.executeUpdate();
				} catch (SQLException e) {
					Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
					throw e;
				}
			}
		}
	}

	/**
	 * Cargar datos cartucho rastreo.
	 *
	 * @param c             the c
	 * @param nombreRastreo the nombre rastreo
	 * @return the datos cartucho rastreo form
	 * @throws SQLException the SQL exception
	 */
	public static DatosCartuchoRastreoForm cargarDatosCartuchoRastreo(Connection c, String nombreRastreo) throws SQLException {
		final DatosCartuchoRastreoForm datosCartuchoRastreoForm = new DatosCartuchoRastreoForm();
		final String sq = "SELECT r.*, lg.*, c.id_cartucho, c.nombre as nombre_cartucho, c.numrastreos, ll.acronimo " + "FROM rastreo r INNER JOIN cartucho_rastreo cr ON r.id_rastreo = cr.id_rastreo "
				+ "INNER JOIN cartucho c  ON c.id_cartucho = cr.id_cartucho " + "JOIN languages lg ON r.id_language = lg.id_language " + "JOIN lista ll ON (r.semillas = ll.id_lista) "
				+ "WHERE r.id_rastreo = ?";
		try (PreparedStatement ps = c.prepareStatement(sq)) {
			ps.setString(1, nombreRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					datosCartuchoRastreoForm.setNombreRastreo(rs.getString("nombre_rastreo"));
					datosCartuchoRastreoForm.setId_rastreo(rs.getInt("id_rastreo"));
					datosCartuchoRastreoForm.setId_cartucho(rs.getInt("id_cartucho"));
					datosCartuchoRastreoForm.setNombre_cart(rs.getString("nombre_cartucho"));
					datosCartuchoRastreoForm.setNumRastreos(rs.getInt("numrastreos"));
					datosCartuchoRastreoForm.setProfundidad(rs.getInt("profundidad"));
					datosCartuchoRastreoForm.setTopN(rs.getInt("topn"));
					String nomRastreo = rs.getString("nombre_rastreo");
					if (nomRastreo.contains("-")) {
						nomRastreo = nomRastreo.substring(0, nomRastreo.indexOf('-'));
					}
					datosCartuchoRastreoForm.setNombre_rastreo(nomRastreo);
					datosCartuchoRastreoForm.setIdSemilla(rs.getLong("semillas"));
					datosCartuchoRastreoForm.setSeedAcronym(rs.getString("acronimo"));
					datosCartuchoRastreoForm.setListaNoRastreable(rs.getString("lista_no_rastreable"));
					datosCartuchoRastreoForm.setListaRastreable(rs.getString("lista_rastreable"));
					datosCartuchoRastreoForm.setIdCuentaCliente(rs.getLong("id_cuenta"));
					datosCartuchoRastreoForm.setIdObservatory(rs.getLong("id_observatorio"));
					datosCartuchoRastreoForm.setPseudoaleatorio(rs.getBoolean("pseudoaleatorio"));
					datosCartuchoRastreoForm.setExhaustive(rs.getBoolean("exhaustive"));
					datosCartuchoRastreoForm.setInDirectory(rs.getBoolean("in_directory"));
					LenguajeForm lenguajeForm = new LenguajeForm();
					lenguajeForm.setId(rs.getLong("r.id_language"));
					lenguajeForm.setKeyName(rs.getString("key_name"));
					lenguajeForm.setCodice(rs.getString("codice"));
					datosCartuchoRastreoForm.setLanguage(lenguajeForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return datosCartuchoRastreoForm;
	}

	/**
	 * Actualiza la fecha de ejecución de un rastreo a la fecha actual.
	 *
	 * @param c         conexión Connection a la base de datos
	 * @param idRastreo identificador long del rastreo a actualizar
	 * @throws SQLException the SQL exception
	 */
	public static void actualizarFechaRastreo(Connection c, long idRastreo) throws SQLException {
		try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET fecha_lanzado = ? WHERE id_rastreo = ?")) {
			pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pst.setLong(2, idRastreo);
			pst.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Actualizar estado rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @param status    the status
	 * @throws SQLException the SQL exception
	 */
	public static void actualizarEstadoRastreo(Connection c, int idRastreo, int status) throws SQLException {
		try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE id_rastreo = ?")) {
			pst.setInt(1, status);
			pst.setInt(2, idRastreo);
			pst.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Cargar fecha rastreo.
	 *
	 * @param c             the c
	 * @param nombreRastreo the nombre rastreo
	 * @return the string
	 * @throws SQLException the SQL exception
	 */
	public static String cargarFechaRastreo(Connection c, String nombreRastreo) throws SQLException {
		String f = "";
		try (PreparedStatement ps = c.prepareStatement("SELECT fecha_lanzado FROM rastreo WHERE nombre_rastreo = ?")) {
			ps.setString(1, nombreRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					try {
						if (rs.getString("fecha_lanzado") == null) {
							f = "No Lanzado";
						} else {
							f = rs.getString("fecha_lanzado");
						}
					} catch (Exception e) {
						f = "No Lanzado";
					}
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return f;
	}

	/**
	 * Existe rastreo.
	 *
	 * @param c             the c
	 * @param nombreRastreo the nombre rastreo
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean existeRastreo(Connection c, String nombreRastreo) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM rastreo WHERE nombre_rastreo = ?")) {
			ps.setString(1, nombreRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Cargar rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @param rastreo   the rastreo
	 * @return the insertar rastreo form
	 * @throws SQLException the SQL exception
	 */
	public static InsertarRastreoForm cargarRastreo(Connection c, int idRastreo, InsertarRastreoForm rastreo) throws SQLException {
		final PropertiesManager pmgr = new PropertiesManager();
		try (PreparedStatement ps = c.prepareStatement("SELECT r.*, l.nombre AS nombreSemilla, l1.lista AS listaRastreable, " + "l2.lista AS listaNoRastreable, cr.id_cartucho FROM rastreo r "
				+ "JOIN cartucho_rastreo cr ON (r.id_rastreo = cr.id_rastreo) " + "LEFT JOIN lista l ON (l.id_lista = r.semillas)" + "LEFT JOIN lista l1 ON (l1.id_lista = r.lista_rastreable)"
				+ "LEFT JOIN lista l2 ON (l2.id_lista = r.lista_no_rastreable)" + "WHERE r.id_rastreo = ?")) {
			ps.setInt(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Código del rastreo
					String nombreRastreo = rs.getString("nombre_rastreo");
					if (nombreRastreo.contains("-")) {
						nombreRastreo = nombreRastreo.substring(0, nombreRastreo.indexOf('-'));
					}
					rastreo.setCuenta_cliente(rs.getLong("id_cuenta"));
					rastreo.setNormaAnalisis(rs.getString("id_guideline"));
					rastreo.setLenguaje(rs.getLong("id_language"));
					rastreo.setCodigo(nombreRastreo);
					DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
					rastreo.setFecha(dateFormat.format(rs.getDate("Fecha")));
					rastreo.setProfundidad(rs.getInt("profundidad"));
					rastreo.setTopN(rs.getInt("topn"));
					rastreo.setSemilla(rs.getString("nombreSemilla"));
					rastreo.setId_semilla(rs.getLong("semillas"));
					rastreo.setId_lista_no_rastreable(rs.getLong("lista_no_rastreable"));
					rastreo.setId_lista_rastreable(rs.getLong("lista_rastreable"));
					rastreo.setListaNoRastreable(rs.getString("listaNoRastreable"));
					rastreo.setListaRastreable(rs.getString("listaRastreable"));
					rastreo.setCuenta_cliente(rs.getLong("id_cuenta"));
					rastreo.setCartucho(rs.getString("id_cartucho"));
					rastreo.setExhaustive(rs.getBoolean("exhaustive"));
					rastreo.setPseudoAleatorio(rs.getBoolean("pseudoaleatorio"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return rastreo;
	}

	/**
	 * Cargar rastreo ver.
	 *
	 * @param c              the c
	 * @param idRastreo      the id rastreo
	 * @param verRastreoForm the ver rastreo form
	 * @return the ver rastreo form
	 * @throws SQLException the SQL exception
	 */
	public static VerRastreoForm cargarRastreoVer(Connection c, long idRastreo, VerRastreoForm verRastreoForm) throws SQLException {
		verRastreoForm.setId_rastreo(idRastreo);
		try (PreparedStatement ps = c.prepareStatement("SELECT * , l.lista AS semilla, l1.lista AS l_lista_rastreable, "
				+ "l2.lista AS l_lista_no_rastreable, cc.nombre AS cuentaCliente, c.nombre AS nombreCartucho " + "FROM rastreo r " + "JOIN cartucho_rastreo cr ON (r.id_rastreo = cr.id_rastreo) "
				+ "JOIN cartucho c ON (cr.id_cartucho = c.id_cartucho) " + "LEFT JOIN cuenta_cliente cc ON (r.id_cuenta = cc.id_cuenta) " + "LEFT JOIN lista l ON (l.id_lista = r.semillas) "
				+ "LEFT JOIN lista l1 ON (l1.id_lista = r.lista_rastreable) " + "LEFT JOIN lista l2 ON (l2.id_lista = r.lista_no_rastreable) " + "WHERE r.Id_Rastreo = ?")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String nombreRastreo = rs.getString("nombre_rastreo");
					if (nombreRastreo.contains("-")) {
						nombreRastreo = nombreRastreo.substring(0, nombreRastreo.indexOf('-'));
					}
					verRastreoForm.setRastreo(nombreRastreo);
					verRastreoForm.setFecha(rs.getString(3));
					if (verRastreoForm.getFecha().endsWith(".0")) {
						verRastreoForm.setFecha(verRastreoForm.getFecha().substring(0, verRastreoForm.getFecha().length() - 2));
					}
					if (rs.getString("semilla") != null) {
						verRastreoForm.setUrl_semilla(convertStringToList(rs.getString("semilla")));
					}
					if (rs.getString("lista_rastreable") != null) {
						verRastreoForm.setUrl_listaRastreable(convertStringToList(rs.getString("l_lista_rastreable")));
					}
					if (rs.getString("lista_no_rastreable") != null) {
						verRastreoForm.setUrl_listaNoRastreable(convertStringToList(rs.getString("l_lista_no_rastreable")));
					}
					verRastreoForm.setProfundidad(rs.getInt(5));
					verRastreoForm.setTopN_ver(rs.getString(6));
					verRastreoForm.setPseudoAleatorio(String.valueOf(rs.getBoolean("pseudoaleatorio")));
					verRastreoForm.setNormaAnalisis(rs.getLong("id_guideline"));
					verRastreoForm.setAutomatico(rs.getInt("automatico"));
					verRastreoForm.setActivo(rs.getLong("activo"));
					verRastreoForm.setInDirectory(rs.getBoolean("in_directory"));
					try {
						if (rs.getString("fecha_lanzado") != null) {
							verRastreoForm.setFechaLanzado(rs.getString("fecha_lanzado"));
							if (verRastreoForm.getFechaLanzado().endsWith(".0")) {
								verRastreoForm.setFechaLanzado(verRastreoForm.getFechaLanzado().substring(0, verRastreoForm.getFechaLanzado().length() - 2));
							}
						}
					} catch (Exception e) {
						// Logger.putLog("Error al añadir un rastreo realizado
						// ", RastreoDAO.class, Logger.LOG_LEVEL_INFO);
					}
					verRastreoForm.setListaNoRastreable(rs.getString("lista_no_rastreable"));
					verRastreoForm.setListaRastreable(rs.getString("lista_rastreable"));
					verRastreoForm.setNombre_cartucho(rs.getString("nombrecartucho"));
					verRastreoForm.setId_cartucho(rs.getInt("id_cartucho"));
					verRastreoForm.setCuentaCliente(rs.getString("cuentacliente"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return verRastreoForm;
	}

	/**
	 * Cargar rastreo ejecutado.
	 *
	 * @param c           the c
	 * @param idExecution the id execution
	 * @return the rastreo ejecutado form
	 * @throws SQLException the SQL exception
	 */
	public static RastreoEjecutadoForm cargarRastreoEjecutado(Connection c, long idExecution) throws SQLException {
		final RastreoEjecutadoForm rastreo = new RastreoEjecutadoForm();
		try (PreparedStatement ps = c.prepareStatement("SELECT r.*, rr.* FROM rastreo r " + "JOIN rastreos_realizados rr ON (r.id_rastreo = rr.id_rastreo) " + "WHERE rr.id = ? ")) {
			ps.setLong(1, idExecution);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rastreo.setId_ejecucion(rs.getLong("id"));
					rastreo.setId_rastreo(rs.getLong("id_rastreo"));
					rastreo.setFecha(CrawlerUtils.formatDate(rs.getDate("rr.fecha")));
					rastreo.setNombre_rastreo(rs.getString("nombre_rastreo"));
					rastreo.setId_cartucho(rs.getLong("rr.id_cartucho"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return rastreo;
	}

	/**
	 * Comprueba que el rastreo es válido para un usuario.
	 *
	 * @param c         conexión Connection a la BD
	 * @param idRastreo identificador del rastreo
	 * @param user      nombre de usuario
	 * @return true si el usuario tiene asociado el cartucho de ese rastreo o false en caso contrario
	 * @throws SQLException the SQL exception
	 */
	public static boolean rastreoValidoParaUsuario(final Connection c, final long idRastreo, final String user) throws SQLException {
		try (PreparedStatement pstmt = c.prepareStatement("SELECT COUNT(*) FROM cartucho_rastreo WHERE id_rastreo = ? and id_cartucho = (SELECT id_cartucho FROM usuario WHERE usuario = ?)")) {
			pstmt.setLong(1, idRastreo);
			pstmt.setString(2, user);
			try (ResultSet rst = pstmt.executeQuery()) {
				if (rst.next()) {
					return rst.getInt(1) != 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Insertar rastreo.
	 *
	 * @param c                   the c
	 * @param insertarRastreoForm the insertar rastreo form
	 * @param isAutomatic         the is automatic
	 * @return the string
	 * @throws SQLException the SQL exception
	 */
	public static String insertarRastreo(Connection c, InsertarRastreoForm insertarRastreoForm, boolean isAutomatic) throws SQLException {
		PropertiesManager pmgr = new PropertiesManager();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idCartucho = -1;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement("SELECT * FROM cartucho WHERE id_cartucho = ?");
			ps.setString(1, insertarRastreoForm.getCartucho());
			rs = ps.executeQuery();
			while (rs.next()) {
				idCartucho = rs.getInt(1);
			}
			DAOUtils.closeQueries(ps, rs);
			insertarRastreoForm.setId_cartucho(idCartucho);
			if (!CartuchoDAO.isCartuchoAccesibilidad(c, idCartucho)) {
				ps = c.prepareStatement(
						"INSERT INTO rastreo (nombre_rastreo, fecha, profundidad, topn, semillas, lista_no_rastreable, lista_rastreable, estado, id_cuenta, pseudoaleatorio, activo, id_language, id_observatorio, automatico, exhaustive, in_directory) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			} else {
				ps = c.prepareStatement(
						"INSERT INTO rastreo (nombre_rastreo, fecha, profundidad, topn, semillas, lista_no_rastreable, lista_rastreable, estado, id_cuenta, pseudoaleatorio, activo, id_language, id_observatorio, automatico, exhaustive, in_directory, id_guideline) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				if (insertarRastreoForm.getNormaAnalisisEnlaces() != null && insertarRastreoForm.getNormaAnalisisEnlaces().equals("1")) {
					if (insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
						ps.setString(17, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
					} else if (insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
						ps.setString(17, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
					} else {
						ps.setString(17, insertarRastreoForm.getNormaAnalisis());
					}
				} else {
					ps.setString(17, insertarRastreoForm.getNormaAnalisis());
				}
			}
			ps.setString(1, insertarRastreoForm.getCodigo());
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setInt(3, insertarRastreoForm.getProfundidad());
			ps.setLong(4, insertarRastreoForm.getTopN());
			ps.setLong(5, insertarRastreoForm.getId_semilla());
			if (insertarRastreoForm.getId_lista_no_rastreable() != 0) {
				ps.setLong(6, insertarRastreoForm.getId_lista_no_rastreable());
			} else {
				ps.setString(6, null);
			}
			if (insertarRastreoForm.getId_lista_rastreable() != 0) {
				ps.setLong(7, insertarRastreoForm.getId_lista_rastreable());
			} else {
				ps.setString(7, null);
			}
			ps.setInt(8, Constants.STATUS_NOT_LAUNCHED);
			if (insertarRastreoForm.getCuenta_cliente() != null) {
				ps.setLong(9, insertarRastreoForm.getCuenta_cliente());
			} else {
				ps.setNull(9, Types.BIGINT);
			}
			ps.setBoolean(10, insertarRastreoForm.isPseudoAleatorio());
			ps.setBoolean(11, insertarRastreoForm.isActive());
			ps.setLong(12, insertarRastreoForm.getLenguaje());
			if (insertarRastreoForm.getId_observatorio() != null) {
				ps.setLong(13, insertarRastreoForm.getId_observatorio());
			} else {
				ps.setString(13, null);
			}
			ps.setBoolean(14, isAutomatic);
			ps.setBoolean(15, insertarRastreoForm.isExhaustive());
			ps.setBoolean(16, insertarRastreoForm.isInDirectory());
			ps.executeUpdate();
			DAOUtils.closeQueries(ps, rs);
			int idRastreo = -1;
			ps = c.prepareStatement("SELECT * FROM rastreo WHERE nombre_rastreo = ?");
			ps.setString(1, insertarRastreoForm.getCodigo());
			rs = ps.executeQuery();
			while (rs.next()) {
				idRastreo = rs.getInt(1);
			}
			DAOUtils.closeQueries(ps, rs);
			insertarRastreoForm.setId_rastreo(idRastreo);
			ps = c.prepareStatement("INSERT INTO cartucho_rastreo(id_cartucho, id_rastreo) VALUES (?, ?)");
			ps.setInt(1, idCartucho);
			ps.setInt(2, idRastreo);
			ps.executeUpdate();
			c.commit();
		} catch (SQLException e) {
			try {
				c.rollback();
			} catch (SQLException e1) {
				Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, rs);
		}
		return "";
	}

	/**
	 * Checks if is automatic crawler.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @return true, if is automatic crawler
	 * @throws SQLException the SQL exception
	 */
	public static boolean isAutomaticCrawler(Connection c, long idRastreo) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT automatico FROM rastreo WHERE id_rastreo = ?")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("automatico") == 1;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Update lists.
	 *
	 * @param c                   the c
	 * @param insertarRastreoForm the insertar rastreo form
	 * @return the insertar rastreo form
	 * @throws Exception the exception
	 */
	private static InsertarRastreoForm updateLists(final Connection c, final InsertarRastreoForm insertarRastreoForm) throws Exception {
		final UpdateListDataForm updateListDataForm = new UpdateListDataForm();
		updateListDataForm.setListaRastreable(insertarRastreoForm.getListaRastreable());
		updateListDataForm.setIdListaRastreable(insertarRastreoForm.getId_lista_rastreable());
		updateListDataForm.setListaNoRastreable(insertarRastreoForm.getListaNoRastreable());
		updateListDataForm.setIdListaNoRastreable(insertarRastreoForm.getId_lista_no_rastreable());
		updateListDataForm.setNombre(insertarRastreoForm.getCodigo());
		SemillaDAO.updateLists(c, updateListDataForm);
		insertarRastreoForm.setId_lista_rastreable(updateListDataForm.getIdListaRastreable());
		insertarRastreoForm.setId_lista_no_rastreable(updateListDataForm.getIdListaNoRastreable());
		insertarRastreoForm.setIdRastreableAntiguo(updateListDataForm.getIdRastreableAntiguo());
		insertarRastreoForm.setIdNoRastreableAntiguo(updateListDataForm.getIdNoRastreableAntiguo());
		return insertarRastreoForm;
	}

	/**
	 * Removes the lists.
	 *
	 * @param c                   the c
	 * @param insertarRastreoForm the insertar rastreo form
	 * @throws Exception the exception
	 */
	private static void removeLists(Connection c, InsertarRastreoForm insertarRastreoForm) throws Exception {
		UpdateListDataForm updateListDataForm = new UpdateListDataForm();
		updateListDataForm.setListaRastreable(insertarRastreoForm.getListaRastreable());
		updateListDataForm.setIdRastreableAntiguo(insertarRastreoForm.getIdRastreableAntiguo());
		updateListDataForm.setListaNoRastreable(insertarRastreoForm.getListaNoRastreable());
		updateListDataForm.setIdNoRastreableAntiguo(insertarRastreoForm.getIdNoRastreableAntiguo());
		SemillaDAO.removeLists(c, updateListDataForm);
	}

	/**
	 * Update rastreo.
	 *
	 * @param insertarRastreoForm the insertar rastreo form
	 * @throws Exception the exception
	 */
	public static void updateRastreo(final InsertarRastreoForm insertarRastreoForm) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			try {
				c.setAutoCommit(false);
				updateLists(c, insertarRastreoForm);
				modificarRastreo(c, false, insertarRastreoForm, insertarRastreoForm.getId_rastreo());
				removeLists(c, insertarRastreoForm);
				c.commit();
				c.setAutoCommit(true);
			} catch (Exception e) {
				c.rollback();
				c.setAutoCommit(true);
				throw e;
			}
		} catch (Exception e) {
			Logger.putLog("Error: ", CuentaUsuarioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Modificar rastreo.
	 *
	 * @param c                   the c
	 * @param esMenu              the es menu
	 * @param insertarRastreoForm the insertar rastreo form
	 * @param idRastreo           the id rastreo
	 * @throws SQLException the SQL exception
	 */
	public static void modificarRastreo(Connection c, boolean esMenu, InsertarRastreoForm insertarRastreoForm, Long idRastreo) throws SQLException {
		PreparedStatement ps = null;
		try {
			PropertiesManager pmgr = new PropertiesManager();
			boolean changeName = true;
			if (insertarRastreoForm.getCuenta_cliente() != null && insertarRastreoForm.getCuenta_cliente() != 0 && !RastreoDAO.isAutomaticCrawler(c, idRastreo)) {
				changeName = false;
			}
			if (!esMenu) {
				if (changeName) {
					ps = c.prepareStatement(
							"UPDATE rastreo SET fecha = ?, profundidad = ?, topn = ?, lista_no_rastreable = ?, lista_rastreable = ?, pseudoaleatorio = ?, id_language = ?, id_guideline = ?, semillas = ?, nombre_rastreo = ?, exhaustive = ?, in_directory = ? WHERE id_rastreo = ?");
					ps.setString(8, null);
					ps.setString(10, insertarRastreoForm.getCodigo());
					ps.setBoolean(11, insertarRastreoForm.isExhaustive());
					ps.setBoolean(12, insertarRastreoForm.isInDirectory());
					ps.setLong(13, idRastreo);
				} else {
					ps = c.prepareStatement(
							"UPDATE rastreo SET fecha = ?, profundidad = ?, topn = ?, lista_no_rastreable = ?, lista_rastreable = ?, pseudoaleatorio = ?, id_language = ?, id_guideline = ?, semillas = ?, exhaustive = ?, in_directory = ? WHERE id_rastreo = ?");
					ps.setBoolean(10, insertarRastreoForm.isExhaustive());
					ps.setBoolean(11, insertarRastreoForm.isInDirectory());
					ps.setLong(12, idRastreo);
				}
				if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(insertarRastreoForm.getCartucho()))) {
					// Incluimos la norma dependiendo de el valor de los enlaces
					// rotos
					if (insertarRastreoForm.getNormaAnalisisEnlaces() != null && insertarRastreoForm.getNormaAnalisisEnlaces().equals("1")) {
						if (insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"))) {
							ps.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.aux.id"));
						} else if (insertarRastreoForm.getNormaAnalisis().equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"))) {
							ps.setString(8, pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.aux.id"));
						} else {
							ps.setString(8, insertarRastreoForm.getNormaAnalisis());
						}
					} else {
						ps.setString(8, insertarRastreoForm.getNormaAnalisis());
					}
				} else {
					ps.setString(8, null);
				}
				ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				ps.setInt(2, insertarRastreoForm.getProfundidad());
				ps.setLong(3, insertarRastreoForm.getTopN());
				if (insertarRastreoForm.getId_lista_no_rastreable() != 0) {
					ps.setLong(4, insertarRastreoForm.getId_lista_no_rastreable());
				} else {
					ps.setString(4, null);
				}
				if (insertarRastreoForm.getId_lista_rastreable() != 0) {
					ps.setLong(5, insertarRastreoForm.getId_lista_rastreable());
				} else {
					ps.setString(5, null);
				}
				ps.setBoolean(6, insertarRastreoForm.isPseudoAleatorio());
				ps.setLong(7, insertarRastreoForm.getLenguaje());
				ps.setLong(9, insertarRastreoForm.getId_semilla());
				ps.setBoolean(11, insertarRastreoForm.isExhaustive());
				ps.executeUpdate();
				if (insertarRastreoForm.getCartucho() != null && !insertarRastreoForm.getCartucho().isEmpty()) {
					ps = c.prepareStatement("UPDATE cartucho_rastreo SET id_cartucho = ? WHERE id_rastreo = ?");
					ps.setInt(1, Integer.parseInt(insertarRastreoForm.getCartucho()));
					ps.setLong(2, idRastreo);
					ps.executeUpdate();
				}
			} else {
				ps = c.prepareStatement("UPDATE rastreo SET profundidad = ?, topn = ?  WHERE id_rastreo = ?");
				ps.setInt(1, insertarRastreoForm.getProfundidad());
				ps.setLong(2, insertarRastreoForm.getTopN());
				ps.setLong(3, idRastreo);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			Logger.putLog("Exception", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Gets the num active crawlings.
	 *
	 * @param conn      the conn
	 * @param cartridge the cartridge
	 * @return the num active crawlings
	 * @throws SQLException the SQL exception
	 */
	public static int getNumActiveCrawlings(Connection conn, int cartridge) throws SQLException {
		try (PreparedStatement ps = conn
				.prepareStatement("SELECT COUNT(*) FROM rastreo r " + "JOIN cartucho_rastreo cr ON (r.id_rastreo = cr.id_rastreo)" + " WHERE estado = ? AND cr.id_cartucho = ?")) {
			ps.setInt(1, Constants.STATUS_LAUNCHED);
			ps.setInt(2, cartridge);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Adds the fulfilled crawling.
	 *
	 * @param conn                   the conn
	 * @param dcrForm                the dcr form
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @param idUser                 the id user
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static Long addFulfilledCrawling(Connection conn, DatosCartuchoRastreoForm dcrForm, Long idFulfilledObservatory, Long idUser) throws SQLException {
		try (PreparedStatement pst = conn.prepareStatement("INSERT INTO rastreos_realizados (id_rastreo, fecha, id_usuario, id_cartucho, id_obs_realizado, id_lista) VALUES (?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setLong(1, dcrForm.getId_rastreo());
			pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pst.setLong(3, idUser);
			pst.setLong(4, dcrForm.getId_cartucho());
			if (idFulfilledObservatory != null) {
				pst.setLong(5, idFulfilledObservatory);
			} else {
				pst.setString(5, null);
			}
			pst.setLong(6, dcrForm.getIdSemilla());
			pst.executeUpdate();
			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al añadir un rastreo realizado: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Adds the fulfilled observatory.
	 *
	 * @param conn          the conn
	 * @param idObservatory the id observatory
	 * @param idCartridge   the id cartridge
	 * @return the long
	 * @throws Exception the exception
	 */
	public static Long addFulfilledObservatory(Connection conn, Long idObservatory, Long idCartridge) throws Exception {
		try (PreparedStatement pst = conn.prepareStatement("INSERT INTO observatorios_realizados (id_observatorio, fecha, id_cartucho) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			pst.setLong(1, idObservatory);
			pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pst.setLong(3, idCartridge);
			pst.executeUpdate();
			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al añadir un rastreo realizado: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the fulfilled crawlings.
	 *
	 * @param conn                   the conn
	 * @param idCrawling             the id crawling
	 * @param searchForm             the search form
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @param pagina                 the pagina
	 * @return the fulfilled crawlings
	 * @throws Exception the exception
	 */
	public static List<FulFilledCrawling> getFulfilledCrawlings(Connection conn, Long idCrawling, CargarRastreosRealizadosSearchForm searchForm, Long idFulfilledObservatory, int pagina)
			throws Exception {
		final List<FulFilledCrawling> crawlings = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT rr.* , u.usuario, c.aplicacion, l.nombre ");
		builder.append("FROM rastreos_realizados rr ");
		builder.append("JOIN usuario u ON (u.id_usuario = rr.id_usuario) ");
		builder.append("JOIN cartucho c ON (rr.id_cartucho = c.id_cartucho) ");
		builder.append("LEFT JOIN lista l ON (rr.id_lista = l.id_lista) ");
		builder.append("WHERE id_rastreo = ? ");
		String query = builder.toString();
		if (searchForm != null) {
			if (searchForm.getInitial_date() != null && !searchForm.getInitial_date().isEmpty()) {
				query += " AND rr.fecha >= ?";
			}
			if (searchForm.getFinal_date() != null && !searchForm.getFinal_date().isEmpty()) {
				query += " AND rr.fecha <= ? ";
			}
			if (searchForm.getCartridge() != null && !searchForm.getCartridge().isEmpty()) {
				query += " AND rr.id_cartucho = ? ";
			}
			if (searchForm.getSeed() != null && !searchForm.getSeed().isEmpty()) {
				query += " AND rr.id_lista = ? ";
			}
		}
		if (idFulfilledObservatory != null) {
			query += " AND rr.id_obs_realizado = ? ";
		}
		query += "ORDER BY fecha DESC LIMIT ? OFFSET ?;";
		int count = 1;
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setLong(count++, idCrawling);
			if (searchForm != null) {
				if (searchForm.getInitial_date() != null && !searchForm.getInitial_date().isEmpty()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					Date date = sdf.parse(searchForm.getInitial_date());
					ps.setTimestamp(count++, new Timestamp(date.getTime()));
				}
				if (searchForm.getFinal_date() != null && !searchForm.getFinal_date().isEmpty()) {
					searchForm.setFinal_date(searchForm.getFinal_date());
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
					Date date = sdf.parse(searchForm.getFinal_date() + " 23:59:59");
					ps.setTimestamp(count++, new Timestamp(date.getTime()));
				}
				if (searchForm.getCartridge() != null && !searchForm.getCartridge().isEmpty()) {
					ps.setLong(count++, Long.parseLong(searchForm.getCartridge()));
				}
				if (searchForm.getSeed() != null && !searchForm.getSeed().isEmpty()) {
					ps.setLong(count++, Long.parseLong(searchForm.getSeed()));
				}
			}
			if (idFulfilledObservatory != null) {
				ps.setLong(count++, idFulfilledObservatory);
			}
			ps.setInt(count++, pagSize);
			ps.setInt(count, resultFrom);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
					fulfilledCrawling.setId(rs.getLong("id"));
					fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
					fulfilledCrawling.setUser(rs.getString("usuario"));
					fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
					fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
					fulfilledCrawling.setCartridge(rs.getString("aplicacion"));
					fulfilledCrawling.setIdFulfilledObservatory(rs.getLong("id_obs_realizado"));
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setNombre(rs.getString("nombre"));
					fulfilledCrawling.setSeed(semillaForm);
					crawlings.add(fulfilledCrawling);
				}
				return crawlings;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the fulfilled crawlings.
	 *
	 * @param conn                   the conn
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the fulfilled crawlings
	 * @throws Exception the exception
	 */
	public static Map<Long, List<FulFilledCrawling>> getFulfilledCrawlings(Connection conn, List<ResultadoSemillaForm> seedsResults, Long idFulfilledObservatory) throws Exception {
		final Map<Long, List<FulFilledCrawling>> results = new HashMap<>();
		boolean isWhere = false;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT rr.* , u.usuario, c.aplicacion, l.nombre ");
		builder.append("FROM rastreos_realizados rr ");
		builder.append("JOIN usuario u ON (u.id_usuario = rr.id_usuario) ");
		builder.append("JOIN cartucho c ON (rr.id_cartucho = c.id_cartucho) ");
		builder.append("LEFT JOIN lista l ON (rr.id_lista = l.id_lista) ");
		String query = builder.toString();
		if (seedsResults.size() != 0) {
			query += "WHERE id_rastreo IN ";
			StringBuilder idStrList = new StringBuilder(" (");
			for (int i = 1; i <= seedsResults.size(); i++) {
				if (seedsResults.size() > i) {
					idStrList.append("?,");
				} else if (seedsResults.size() == i) {
					idStrList.append("?) ");
				}
			}
			query += idStrList;
			isWhere = true;
		}
		if (idFulfilledObservatory != null) {
			if (isWhere) {
				query += " AND rr.id_obs_realizado = ? ";
			} else {
				query += " WHERE rr.id_obs_realizado = ? ";
			}
		}
		int count = 1;
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			for (ResultadoSemillaForm seedsResult : seedsResults) {
				ps.setLong(count++, Long.parseLong(seedsResult.getIdCrawling()));
			}
			if (idFulfilledObservatory != null) {
				ps.setLong(count, idFulfilledObservatory);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
					fulfilledCrawling.setId(rs.getLong("id"));
					fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
					fulfilledCrawling.setUser(rs.getString("usuario"));
					fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
					fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
					fulfilledCrawling.setCartridge(rs.getString("aplicacion"));
					fulfilledCrawling.setIdFulfilledObservatory(rs.getLong("id_obs_realizado"));
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setNombre(rs.getString("nombre"));
					fulfilledCrawling.setSeed(semillaForm);
					if (!results.containsKey(rs.getLong("id_rastreo"))) {
						results.put(rs.getLong("id_rastreo"), new ArrayList<FulFilledCrawling>());
					}
					results.get(rs.getLong("id_rastreo")).add(fulfilledCrawling);
				}
				return results;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the fulfilled crawlings 2.
	 *
	 * @param conn                   the conn
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the fulfilled crawlings 2
	 * @throws Exception the exception
	 */
	public static Map<Long, List<FulFilledCrawling>> getFulfilledCrawlings2(Connection conn, List<ResultadoSemillaFullForm> seedsResults, Long idFulfilledObservatory) throws Exception {
		final Map<Long, List<FulFilledCrawling>> results = new HashMap<>();
		boolean isWhere = false;
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT rr.* , u.usuario, c.aplicacion, l.nombre ");
		builder.append("FROM rastreos_realizados rr ");
		builder.append("JOIN usuario u ON (u.id_usuario = rr.id_usuario) ");
		builder.append("JOIN cartucho c ON (rr.id_cartucho = c.id_cartucho) ");
		builder.append("LEFT JOIN lista l ON (rr.id_lista = l.id_lista) ");
		String query = builder.toString();
		if (seedsResults.size() != 0) {
			query += "WHERE id_rastreo IN ";
			StringBuilder idStrList = new StringBuilder(" (");
			for (int i = 1; i <= seedsResults.size(); i++) {
				if (seedsResults.size() > i) {
					idStrList.append("?,");
				} else if (seedsResults.size() == i) {
					idStrList.append("?) ");
				}
			}
			query += idStrList;
			isWhere = true;
		}
		if (idFulfilledObservatory != null) {
			if (isWhere) {
				query += " AND rr.id_obs_realizado = ? ";
			} else {
				query += " WHERE rr.id_obs_realizado = ? ";
			}
		}
		int count = 1;
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			for (ResultadoSemillaForm seedsResult : seedsResults) {
				ps.setLong(count++, Long.parseLong(seedsResult.getIdCrawling()));
			}
			if (idFulfilledObservatory != null) {
				ps.setLong(count, idFulfilledObservatory);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
					fulfilledCrawling.setId(rs.getLong("id"));
					fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
					fulfilledCrawling.setUser(rs.getString("usuario"));
					fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
					fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
					fulfilledCrawling.setCartridge(rs.getString("aplicacion"));
					fulfilledCrawling.setIdFulfilledObservatory(rs.getLong("id_obs_realizado"));
					SemillaForm semillaForm = new SemillaForm();
					semillaForm.setNombre(rs.getString("nombre"));
					fulfilledCrawling.setSeed(semillaForm);
					if (!results.containsKey(rs.getLong("id_rastreo"))) {
						results.put(rs.getLong("id_rastreo"), new ArrayList<FulFilledCrawling>());
					}
					results.get(rs.getLong("id_rastreo")).add(fulfilledCrawling);
				}
				return results;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the old crawlings.
	 *
	 * @param conn    the conn
	 * @param numDays the num days
	 * @return the old crawlings
	 * @throws Exception the exception
	 */
	public static List<FulFilledCrawling> getOldCrawlings(Connection conn, int numDays) throws Exception {
		final List<FulFilledCrawling> crawlings = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rastreos_realizados rr " + "JOIN observatorios_realizados ob ON (rr.id_obs_realizado = ob.id) " + "WHERE rr.fecha > ?")) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - numDays);
			ps.setDate(1, new java.sql.Date(calendar.getTimeInMillis()));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					FulFilledCrawling fulfilledCrawling = new FulFilledCrawling();
					fulfilledCrawling.setId(rs.getLong("id"));
					fulfilledCrawling.setIdCrawling(rs.getLong("id_rastreo"));
					fulfilledCrawling.setIdObservatory(rs.getLong("id_observatorio"));
					fulfilledCrawling.setIdCartridge(rs.getLong("id_cartucho"));
					fulfilledCrawling.setUser(rs.getString("id_usuario"));
					fulfilledCrawling.setDate(rs.getTimestamp("fecha"));
					crawlings.add(fulfilledCrawling);
				}
			}
			return crawlings;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the nombre norma.
	 *
	 * @param conn    the conn
	 * @param idNorma the id norma
	 * @return the nombre norma
	 * @throws SQLException the SQL exception
	 */
	public static String getNombreNorma(Connection conn, long idNorma) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM tguidelines WHERE cod_guideline = ?;")) {
			ps.setLong(1, idNorma);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("des_guideline").substring(0, rs.getString("des_guideline").length() - 4).toUpperCase();
				}
				return null;
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Convert string to list.
	 *
	 * @param lista the lista
	 * @return the list
	 */
	private static List<String> convertStringToList(String lista) {
		List<String> urlsList = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(lista, ";");
		while (tokenizer.hasMoreTokens()) {
			urlsList.add(tokenizer.nextToken());
		}
		return urlsList;
	}

	/**
	 * Enable disable crawler.
	 *
	 * @param conn      the conn
	 * @param idCrawler the id crawler
	 * @param activo    the activo
	 * @throws SQLException the SQL exception
	 */
	public static void enableDisableCrawler(Connection conn, long idCrawler, boolean activo) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("UPDATE rastreo SET activo = ? WHERE id_rastreo = ?")) {
			ps.setBoolean(1, activo);
			ps.setLong(2, idCrawler);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the id seed by id rastreo.
	 *
	 * @param c         the c
	 * @param idRastreo the id rastreo
	 * @return the id seed by id rastreo
	 * @throws SQLException the SQL exception
	 */
	public static long getIdSeedByIdRastreo(Connection c, long idRastreo) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT semillas FROM rastreo WHERE id_rastreo = ?")) {
			ps.setLong(1, idRastreo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("semillas");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return -1;
	}

	/**
	 * Update crawler name.
	 *
	 * @param c         the c
	 * @param name      the name
	 * @param crawlerId the crawler id
	 * @throws Exception the exception
	 */
	public static void updateCrawlerName(Connection c, String name, long crawlerId) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("UPDATE rastreo SET nombre_rastreo = ? WHERE id_rastreo = ?")) {
			ps.setString(1, name);
			ps.setLong(2, crawlerId);
			ps.executeUpdate();
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the crawler from seed and observatory.
	 *
	 * @param c             the c
	 * @param idSeed        the id seed
	 * @param idObservatory the id observatory
	 * @return the crawler from seed and observatory
	 * @throws Exception the exception
	 */
	public static Long getCrawlerFromSeedAndObservatory(Connection c, long idSeed, long idObservatory) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("SELECT id_rastreo FROM rastreo r WHERE r.id_observatorio = ? AND r.semillas = ?")) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idSeed);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the crawler category ids.
	 *
	 * @param connR          the conn R
	 * @param idObservatorio the id observatorio
	 * @param idCategoria    the id categoria
	 * @return the crawler category ids
	 * @throws Exception the exception
	 */
	public static List<Long> getCrawlerCategoryIds(Connection connR, long idObservatorio, long idCategoria) throws Exception {
		final List<Long> crawlerIds = new ArrayList<>();
		// RECUPERAMOS LOS IDS DE LOS RASTREOS PARA EL OBSERVATORIO
		try (PreparedStatement ps = connR.prepareStatement(
				"SELECT id_rastreo FROM rastreo r " + "JOIN observatorio o ON (r.id_observatorio = o.id_observatorio) " + "JOIN observatorio_categoria oc ON (oc.id_observatorio = o.id_observatorio)"
						+ "WHERE r.id_observatorio = ? AND oc.id_categoria = ? AND r.semillas IN (" + "SELECT id_lista FROM lista WHERE id_categoria = ?)")) {
			ps.setLong(1, idObservatorio);
			ps.setLong(2, idCategoria);
			ps.setLong(3, idCategoria);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("id_rastreo"));
				}
				return crawlerIds;
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the recurrence.
	 *
	 * @param conn         the conn
	 * @param idRecurrence the id recurrence
	 * @return the recurrence
	 * @throws Exception the exception
	 */
	public static PeriodicidadForm getRecurrence(Connection conn, long idRecurrence) throws Exception {
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM periodicidad WHERE id_periodicidad = ?")) {
			ps.setLong(1, idRecurrence);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					PeriodicidadForm periodicidadForm = new PeriodicidadForm();
					periodicidadForm.setId(rs.getLong("id_periodicidad"));
					periodicidadForm.setNombre(rs.getString("nombre"));
					periodicidadForm.setCronExpression(rs.getString("cronExpression"));
					periodicidadForm.setDias(rs.getInt("dias"));
					return periodicidadForm;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the fullfilled crawling execution.
	 *
	 * @param conn        the conn
	 * @param idExecution the id execution
	 * @return the fullfilled crawling execution
	 * @throws SQLException the SQL exception
	 */
	public static FulfilledCrawlingForm getFullfilledCrawlingExecution(Connection conn, long idExecution) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT rr.*, l.*, cl.*, cm.* FROM rastreos_realizados rr " + "JOIN lista l ON (rr.id_lista = l.id_lista) "
				+ "LEFT JOIN categorias_lista cl ON (l.id_categoria = cl.id_categoria) " 
				+ "LEFT JOIN complejidades_lista cm ON (l.id_complejidad = cm.id_complejidad) " + 
				"WHERE id = ? ")) {
			ps.setLong(1, idExecution);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final FulfilledCrawlingForm form = new FulfilledCrawlingForm();
					form.setId(rs.getString("rr.id"));
					form.setDate(CrawlerUtils.formatDate(rs.getDate("fecha")));
					form.setIdCrawling(String.valueOf(rs.getLong("id_rastreo")));
					form.setIdCartridge(String.valueOf(rs.getLong("id_cartucho")));
					final SemillaForm semilla = new SemillaForm();
					semilla.setId(rs.getLong("l.id_lista"));
					semilla.setAcronimo(rs.getString("acronimo"));
					semilla.setNombre(rs.getString("l.nombre"));
					// Multidependencia
					semilla.setListaUrlsString(rs.getString("l.lista"));
					final CategoriaForm categoria = new CategoriaForm();
					categoria.setName(rs.getString("cl.nombre"));
					categoria.setId(rs.getString("cl.id_categoria"));
					categoria.setOrden(rs.getInt("cl.orden"));
					semilla.setCategoria(categoria);
					
					//Complejidad
					final ComplejidadForm complejidad = new ComplejidadForm();
					complejidad.setId(rs.getString("cm.id_complejidad"));
					complejidad.setName(rs.getString("cm.nombre"));
					semilla.setComplejidad(complejidad);
					form.setSeed(semilla);
					return form;
				}
			}
			return null;
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Sets the observatory execution to crawler execution.
	 *
	 * @param conn             the conn
	 * @param idObsExecution   the id obs execution
	 * @param idExecuteCrawler the id execute crawler
	 * @throws SQLException the SQL exception
	 */
	public static void setObservatoryExecutionToCrawlerExecution(Connection conn, long idObsExecution, long idExecuteCrawler) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("UPDATE rastreos_realizados SET id_obs_realizado = ? WHERE id = ?")) {
			ps.setLong(1, idObsExecution);
			ps.setLong(2, idExecuteCrawler);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the executed crawling.
	 *
	 * @param c          the c
	 * @param idCrawling the id crawling
	 * @param idSeed     the id seed
	 * @return the executed crawling
	 * @throws SQLException the SQL exception
	 */
	public static FulfilledCrawlingForm getExecutedCrawling(Connection c, Long idCrawling, Long idSeed) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT rr.id, rr.fecha, rr.id_rastreo, rr.id_cartucho FROM rastreos_realizados rr WHERE id_rastreo = ? AND id_lista = ? ORDER BY id DESC")) {
			ps.setLong(1, idCrawling);
			ps.setLong(2, idSeed);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final FulfilledCrawlingForm form = new FulfilledCrawlingForm();
					form.setId(rs.getString("id"));
					form.setDate(CrawlerUtils.formatDate(rs.getDate("fecha")));
					form.setIdCrawling(String.valueOf(rs.getLong("id_rastreo")));
					form.setIdCartridge(String.valueOf(rs.getLong("id_cartucho")));
					return form;
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Recuperamos los rastreos que no estén marcados como finalizados.
	 *
	 * @param c              the c
	 * @param idObservatory  the id observatory
	 * @param idObsRealizado the id obs realizado
	 * @return the pending crawler from seed and observatory
	 * @throws Exception the exception
	 */
	public static List<Long> getPendingCrawlerFromSeedAndObservatory(Connection c, Long idObservatory, Long idObsRealizado) throws Exception {
		final List<Long> crawlerIds = new ArrayList<>();
		// Union de rastreos no realizados y rastreos empezados pero no
		// terminados (<> estado 4)
		try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT u.id_rastreo FROM (" + "	(SELECT r.id_rastreo FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo NOT IN ("
				+ "		SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE id_obs_realizado = ?) AND r.activo = 1 )" + "	UNION ALL"
				+ "	(SELECT r.id_rastreo FROM rastreo r WHERE r.id_observatorio = ? AND r.estado <> 4 AND r.activo = 1)" + "	) u ORDER BY u.id_rastreo ASC")) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObsRealizado);
			ps.setLong(3, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("id_rastreo"));
				}
				return crawlerIds;
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Sets the score and level crawling.
	 *
	 * @param c                 the c
	 * @param idFullfilledCrawl the id fullfilled crawl
	 * @param score             the score
	 * @param level             the level
	 * @throws Exception the exception
	 */
	public static void setScoreAndLevelCrawling(Connection c, Long idFullfilledCrawl, String score, String level) throws Exception {
		try (PreparedStatement ps = c.prepareStatement("UPDATE rastreos_realizados SET score= ?, level = ? WHERE id=?")) {
			ps.setString(1, score);
			ps.setString(2, level);
			ps.setLong(3, idFullfilledCrawl);
			ps.executeUpdate();
		} catch (Exception e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Recuperamos los rastreos que no estén marcados como finalizados.
	 *
	 * @param c              the c
	 * @param idObservatory  the id observatory
	 * @param idObsRealizado the id obs realizado
	 * @return the pending crawler from seed and observatory
	 * @throws Exception the exception
	 */
	public static List<SemillaForm> getFinishCrawlerFromSeedAndObservatoryWithoutAnalisis(Connection c, Long idObservatory, Long idObsRealizado) throws Exception {
		final List<Long> crawlerIds = new ArrayList<>();
		// Union de rastreos no realizados y rastreos empezados pero no
		// terminados (<> estado 4)
		String query = "SELECT DISTINCT u.semillas  FROM ("
				+ "(SELECT r.semillas FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo  IN (SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE id_obs_realizado = ? ) AND r.activo = 1 AND r.estado = 3 ) "
				+ "UNION ALL "
				+ "(SELECT r.semillas FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo  IN (SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE rr.id_obs_realizado = ? AND rr.id not IN (select ta.cod_rastreo as id_rastreo from tanalisis ta))    AND r.activo = 1  )"
				+ ") u ORDER BY u.semillas ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObsRealizado);
			ps.setLong(3, idObservatory);
			ps.setLong(4, idObsRealizado);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("semillas"));
				}
				if (crawlerIds.size() > 0) {
					return SemillaDAO.getSeedByIds(c, crawlerIds);
				} else {
					return new ArrayList<>();
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the finish crawler from seed and observatory not crawled yet.
	 *
	 * @param c              the c
	 * @param idObservatory  the id observatory
	 * @param idObsRealizado the id obs realizado
	 * @return the finish crawler from seed and observatory not crawled yet
	 * @throws Exception the exception
	 */
	public static List<SemillaForm> getFinishCrawlerFromSeedAndObservatoryNotCrawledYet(Connection c, Long idObservatory, Long idObsRealizado) throws Exception {
		final List<Long> crawlerIds = new ArrayList<>();
		// Union de rastreos no realizados y rastreos empezados pero no
		// terminados (<> estado 4)
		try (PreparedStatement ps = c.prepareStatement("SELECT DISTINCT u.semillas  FROM (" + "	(SELECT r.semillas FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo NOT IN ("
				+ "		SELECT rr.id_rastreo FROM  rastreos_realizados rr WHERE id_obs_realizado = ?) AND r.activo = 1 )" + "	UNION ALL"
				+ "	(SELECT r.semillas FROM rastreo r WHERE r.id_observatorio = ? AND r.estado = 1 AND r.activo = 1)" + "	) u ORDER BY u.semillas ASC")) {
			ps.setLong(1, idObservatory);
			ps.setLong(2, idObsRealizado);
			ps.setLong(3, idObservatory);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					crawlerIds.add(rs.getLong("semillas"));
				}
				if (crawlerIds.size() > 0) {
					return SemillaDAO.getSeedByIds(c, crawlerIds);
				} else {
					return new ArrayList<>();
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
