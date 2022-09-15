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
package es.inteco.rastreador2.dao.basic.service;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.opencsv.CSVWriter;

import es.gob.oaw.basicservice.historico.BasicServiceResultado;
import es.gob.oaw.rastreador2.actionform.diagnostico.ServicioDiagnosticoForm;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;

/**
 * The Class DiagnosisDAO.
 */
public final class DiagnosisDAO {
	/**
	 * Instantiates a new diagnosis DAO.
	 */
	private DiagnosisDAO() {
	}

	/**
	 * Update status.
	 *
	 * @param conn       the conn
	 * @param idAnalysis the id analysis
	 * @param status     the status
	 */
	public static void updateStatus(final Connection conn, final long idAnalysis, final String status) {
		try (PreparedStatement ps = conn.prepareStatement("UPDATE basic_service SET status = ?, send_date = ? WHERE id = ?")) {
			ps.setString(1, status);
			if (Constants.BASIC_SERVICE_STATUS_FINISHED.equals(status)) {
				ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			} else {
				ps.setTimestamp(2, null);
			}
			ps.setLong(3, idAnalysis);
			ps.executeUpdate();
		} catch (Exception e) {
			Logger.putLog("Error al actualizar el estado de la petición del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Insert basic services.
	 *
	 * @param conn             the conn
	 * @param basicServiceForm the basic service form
	 * @param status           the status
	 * @return the long
	 */
	public static long insertBasicServices(final Connection conn, final BasicServiceForm basicServiceForm, final String status) {
		try (PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO basic_service (usr, language, domain, email, depth, width, report, date, status, scheduling_date, analysis_type, in_directory, register_result, complexity, filename, depthReport) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, basicServiceForm.getUser());
			ps.setString(2, basicServiceForm.getLanguage());
			if (basicServiceForm.getContents() != null && !basicServiceForm.getContents().isEmpty() && StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
				StringBuilder sb = new StringBuilder(basicServiceForm.getFileName());
				sb.append("\n");
				sb.append(basicServiceForm.getDomain());
				ps.setString(3, sb.toString());
				ps.setString(11, BasicServiceAnalysisType.MIXTO.getLabel());
			} else if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
				ps.setString(3, basicServiceForm.getDomain());
				ps.setString(11, basicServiceForm.getAnalysisType().getLabel());
			} else if (basicServiceForm.getContents() != null && !basicServiceForm.getContents().isEmpty()) {
				ps.setString(3, basicServiceForm.getFileName());
				ps.setString(11, BasicServiceAnalysisType.CODIGO_FUENTE_MULTIPLE.getLabel());
			} else if (StringUtils.isNotEmpty(basicServiceForm.getContent())) {
				// ps.setString(3, BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()));
				ps.setString(3, basicServiceForm.getFileName());
				ps.setString(11, BasicServiceAnalysisType.CODIGO_FUENTE.getLabel());
			}
			ps.setString(4, basicServiceForm.getEmail());
			if (!org.apache.commons.lang3.StringUtils.isEmpty(basicServiceForm.getComplexity()) && !"0".equalsIgnoreCase(basicServiceForm.getComplexity())) {
				String complex = basicServiceForm.getComplexity();
				ComplejidadForm cx = ComplejidadDAO.getById(conn, complex);
				ps.setString(5, String.valueOf(cx.getProfundidad()));
				ps.setString(6, String.valueOf(cx.getAmplitud()));
			} else {
				ps.setString(5, basicServiceForm.getProfundidad());
				ps.setString(6, basicServiceForm.getAmplitud());
			}
			ps.setString(7, basicServiceForm.getReport());
			ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
			ps.setString(9, status);
			if (basicServiceForm.getSchedulingDate() != null) {
				ps.setTimestamp(10, new Timestamp(basicServiceForm.getSchedulingDate().getTime()));
			} else {
				ps.setTimestamp(10, null);
			}
			ps.setBoolean(12, basicServiceForm.isInDirectory());
			ps.setBoolean(13, basicServiceForm.isRegisterAnalysis());
			ps.setString(14, basicServiceForm.getComplexity());
			ps.setString(15, basicServiceForm.getFileName());
			ps.setString(16, basicServiceForm.getDepthReport());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return 0;
	}

	/**
	 * Gets the basic service request by id.
	 *
	 * @param conn the conn
	 * @param id   the id
	 * @return the basic service request by id
	 */
	public static BasicServiceForm getBasicServiceRequestById(final Connection conn, final long id) {
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM basic_service WHERE id=?")) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final BasicServiceForm basicServiceForm = new BasicServiceForm();
					basicServiceForm.setId(rs.getLong("id"));
					basicServiceForm.setDomain("");
					basicServiceForm.setName("");
					basicServiceForm.setUser(rs.getString("usr"));
					basicServiceForm.setEmail(rs.getString("email"));
					basicServiceForm.setLanguage(rs.getString("language"));
					basicServiceForm.setAmplitud(rs.getString("width"));
					basicServiceForm.setProfundidad(rs.getString("depth"));
					basicServiceForm.setReport(rs.getString("report"));
					basicServiceForm.setDate(rs.getTimestamp("date"));
					basicServiceForm.setInDirectory(rs.getBoolean("in_directory"));
					basicServiceForm.setRegisterAnalysis(rs.getBoolean("register_result"));
					basicServiceForm.setAnalysisType(BasicServiceAnalysisType.parseString(rs.getString("analysis_type")));
					if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.URL) {
						basicServiceForm.setDomain(rs.getString("domain"));
						basicServiceForm.setName(new URL(basicServiceForm.getDomain()).getAuthority());
					} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.LISTA_URLS) {
						basicServiceForm.setDomain(rs.getString("domain"));
					} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.MIXTO) {
						basicServiceForm.setDomain(cleanUrls(rs.getString("domain")));
					}
					basicServiceForm.setComplexity(rs.getString("complexity"));
					basicServiceForm.setFileName(rs.getString("filename"));
					basicServiceForm.setDepthReport(rs.getString("depthReport"));
					return basicServiceForm;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Gets the basic service request by status.
	 *
	 * @param conn   the conn
	 * @param status the status
	 * @return the basic service request by status
	 */
	public static List<BasicServiceForm> getBasicServiceRequestByStatus(Connection conn, String status) {
		final List<BasicServiceForm> results = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM basic_service WHERE status LIKE ?")) {
			ps.setString(1, status);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BasicServiceForm basicServiceForm = new BasicServiceForm();
					basicServiceForm.setId(rs.getLong("id"));
					basicServiceForm.setDomain(rs.getString("domain"));
					basicServiceForm.setUser(rs.getString("usr"));
					basicServiceForm.setEmail(rs.getString("email"));
					basicServiceForm.setLanguage(rs.getString("language"));
					basicServiceForm.setAmplitud(rs.getString("width"));
					basicServiceForm.setProfundidad(rs.getString("depth"));
					basicServiceForm.setReport(rs.getString("report"));
					basicServiceForm.setSchedulingDate(rs.getTimestamp("scheduling_date"));
					basicServiceForm.setInDirectory(rs.getBoolean("in_directory"));
					basicServiceForm.setRegisterAnalysis(rs.getBoolean("register_result"));
					basicServiceForm.setDate(rs.getTimestamp("date"));
					basicServiceForm.setDepthReport(rs.getString("depthReport"));
					results.add(basicServiceForm);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return results;
	}

	/**
	 * Gets the basic service request CSV.
	 *
	 * @param conn      the conn
	 * @param startDate the start date
	 * @param endDate   the end date
	 * @return the basic service request CSV
	 */
	public static String getBasicServiceRequestCSV(final Connection conn, final java.util.Date startDate, final java.util.Date endDate) {
		try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM basic_service WHERE date BETWEEN ? AND ?")) {
			ps.setDate(1, new Date(startDate.getTime()));
			ps.setDate(2, new Date(endDate.getTime()));
			try (ResultSet rs = ps.executeQuery()) {
				Locale.setDefault(new Locale("es", "es"));
				final StringWriter stringWriter = new StringWriter();
				final CSVWriter writer = new CSVWriter(stringWriter, ';');
				writer.writeAll(rs, true);
				writer.close();
				return stringWriter.toString();
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar en CSV los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return "";
	}

	/**
	 * Delete analysis.
	 *
	 * @param conn      the conn
	 * @param entity    the entity
	 * @param idCrawler the id crawler
	 * @throws SQLException the SQL exception
	 */
	public static void deleteAnalysis(final Connection conn, final String entity, final long idCrawler) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tanalisis WHERE nom_entidad = ? AND cod_rastreo = ?")) {
			ps.setString(1, entity);
			ps.setLong(2, idCrawler);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the evaluation ids.
	 *
	 * @param idCrawling the id crawling
	 * @return the evaluation ids
	 * @throws Exception the exception
	 */
	public static List<Long> getEvaluationIds(final Long idCrawling) throws Exception {
		final List<Long> analysisIds = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT cod_analisis FROM tanalisis WHERE cod_rastreo = ?")) {
			ps.setLong(1, idCrawling);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					analysisIds.add(rs.getLong("cod_analisis"));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return analysisIds;
	}

	/**
	 * Gets the historico resultados.
	 *
	 * @param url    the url
	 * @param type   the type
	 * @param width  the width
	 * @param depth  the depth
	 * @param report the report
	 * @return the historico resultados
	 */
	public static List<BasicServiceResultado> getHistoricoResultados(final String url, final BasicServiceAnalysisType type, String width, String depth, String report) {
		final List<BasicServiceResultado> results = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));
		try (Connection conn = DataBaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(getHistoricoResultadosQueryByType(type))) {
			ps.setString(1, url);
			if (BasicServiceAnalysisType.URL.equals(type)) {
				ps.setInt(2, Integer.valueOf(depth));
				ps.setInt(3, Integer.valueOf(width));
				// Por si viene con el nobroken (que no debería)
				ps.setString(4, report.replace("-nobroken", "") + "%");
				ps.setInt(5, 3);// Límite de resultados = 3
			} else {
				ps.setInt(2, 3); // Límite de resultados = 3
				ps.setString(3, report);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final BasicServiceResultado basicServiceResultado = new BasicServiceResultado();
					basicServiceResultado.setId(String.valueOf(rs.getLong("id")));
					basicServiceResultado.setDate(dateFormat.format(rs.getTimestamp("date")));
					results.add(basicServiceResultado);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al acceder al historico de resultados del servicio de diagnostico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return results;
	}

	/**
	 * Gets the historico resultados query by type.
	 *
	 * @param type the type
	 * @return the historico resultados query by type
	 */
	private static String getHistoricoResultadosQueryByType(final BasicServiceAnalysisType type) {
		if (BasicServiceAnalysisType.URL == type) {
			return "SELECT id, date FROM basic_service WHERE domain LIKE ? AND status='finished' AND depth=? AND width=? AND analysis_type='url' AND register_result=TRUE AND report LIKE ? ORDER BY date DESC LIMIT ?";
		} else if (BasicServiceAnalysisType.LISTA_URLS == type) {
			return "SELECT id, date FROM basic_service WHERE domain REGEXP ? AND status='finished' AND analysis_type='lista_urls' AND register_result=TRUE AND report LIKE ? ORDER BY date DESC LIMIT ?";
		}
		return null;
	}

	/**
	 * Actualiza la información de histórico del servicio de diagnóstico y desmarca el registro de resultado guardado.
	 *
	 * @param conn           conexión a la base de datos
	 * @param idBasicService el identificador de la solicitud al servicio de diagnósitco
	 * @throws SQLException si no se pudo realizar la actualización en la base de datos
	 */
	public static void deregisterResult(final Connection conn, final long idBasicService) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("UPDATE basic_service SET register_result = FALSE WHERE id = ?")) {
			ps.setLong(1, idBasicService);
			ps.executeUpdate();
		}
	}

	/**
	 * Find.
	 *
	 * @param conn   the conn
	 * @param pagina the pagina
	 * @param search the search
	 * @return the list
	 */
	public static List<ServicioDiagnosticoForm> find(Connection conn, int pagina, final ServicioDiagnosticoForm search) {
		List<ServicioDiagnosticoForm> results = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
		final int resultFrom = pagSize * pagina;
		StringBuilder query = new StringBuilder(
				"SELECT (select distinct(nombre) from ambitos_lista where id_ambito =(select id_ambito from lista where lista = domain and  id_ambito is not null limit 1) limit 1) as ambito, id,usr,language,domain,email,\n"
						+ "IF(complexity > 0 AND depth IS NULL, (select profundidad from complejidades_lista where id_complejidad = complexity), depth) as depth,\n"
						+ "IF(complexity > 0 AND width IS NULL, (select amplitud from complejidades_lista where id_complejidad = complexity), width) as width,\n"
						+ "report,date,send_date, status,scheduling_date,analysis_type,in_directory,register_result, (select nombre from complejidades_lista where id_complejidad = complexity) as complexity_name, depthReport\n"
						+ "FROM basic_service WHERE 1=1 ");
		addSearchParameters(search, query);
		query.append("ORDER BY id DESC");
		query.append(" LIMIT " + pagSize + " OFFSET " + resultFrom);
		try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
			int nextParameterCount = 1;
			setSearchParameters(search, ps, nextParameterCount);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ServicioDiagnosticoForm result = getFormFromResultSet(rs);
					results.add(result);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar en CSV los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return results;
	}

	/**
	 * Count.
	 *
	 * @param conn   the conn
	 * @param search the search
	 * @return the integer
	 */
	public static Integer count(Connection conn, final ServicioDiagnosticoForm search) {
		StringBuilder query = new StringBuilder("SELECT count(*) FROM basic_service WHERE 1=1 ");
		addSearchParameters(search, query);
		try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
			int nextParameterCount = 1;
			setSearchParameters(search, ps, nextParameterCount);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar en CSV los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return 0;
	}

	/**
	 * Gets the average time.
	 *
	 * @param conn   the conn
	 * @param search the search
	 * @return the average time
	 */
	public static Float getAverageTime(Connection conn, final ServicioDiagnosticoForm search) {
		StringBuilder query = new StringBuilder("SELECT AVG(TIME_TO_SEC(TIMEDIFF(send_date,date))) AS timediff FROM  basic_service WHERE 1=1 ");
		addSearchParameters(search, query);
		try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
			int nextParameterCount = 1;
			setSearchParameters(search, ps, nextParameterCount);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getFloat(1);
				} else {
					return 0f;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar en CSV los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return 0f;
	}

	/**
	 * Gets the basic service request CSV.
	 *
	 * @param conn   the conn
	 * @param search the search
	 * @return the basic service request CSV
	 */
	public static String getCSV(final Connection conn, final ServicioDiagnosticoForm search) {
		// StringBuilder query = new StringBuilder("SELECT * FROM basic_service WHERE 1=1 ");
		StringBuilder query = new StringBuilder(
				"SELECT id," + "usr," + "language," + "domain," + "email," + "depth," + "width," + "(select lower(nombre) from complejidades_lista where id_complejidad = complexity) as complexity,"
						+ "report," + "date," + "status," + "send_date," + "scheduling_date," + "analysis_type," + "in_directory," + "register_result" + " FROM basic_service WHERE 1=1");
		addSearchParameters(search, query);
		try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
			int nextParameterCount = 1;
			setSearchParameters(search, ps, nextParameterCount);
			try (ResultSet rs = ps.executeQuery()) {
				Locale.setDefault(new Locale("es", "es"));
				final StringWriter stringWriter = new StringWriter();
				final CSVWriter writer = new CSVWriter(stringWriter, ';');
				writer.writeAll(rs, true);
				writer.close();
				return stringWriter.toString();
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar en CSV los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return "";
	}

	/**
	 * Gets the form from result set.
	 *
	 * @param rs the rs
	 * @return the form from result set
	 * @throws SQLException   the SQL exception
	 * @throws ParseException the parse exception
	 */
	private static ServicioDiagnosticoForm getFormFromResultSet(ResultSet rs) throws SQLException, ParseException {
		// 2019-11-19 10:38:26.0
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ServicioDiagnosticoForm result = new ServicioDiagnosticoForm();
		result.setId(rs.getLong("id"));
		result.setUser(rs.getString("usr"));
		result.setLanguage(rs.getString("language"));
		result.setDomain(rs.getString("domain"));
		result.setEmail(rs.getString("email"));
		result.setDepth(rs.getInt("depth"));
		result.setWidth(rs.getInt("width"));
		result.setReport(rs.getString("report"));
		result.setStatus(rs.getString("status"));
		result.setSchedulingDate(rs.getDate("scheduling_date"));
		result.setAnalysisType(rs.getString("analysis_type"));
		result.setInDirectory(rs.getInt("in_directory"));
		result.setRegisterResult(rs.getInt("register_result"));
		result.setType(rs.getString("ambito"));
		if (!org.apache.commons.lang3.StringUtils.isEmpty(rs.getString("date"))) {
			result.setDate(format.parse(rs.getString("date")));
		}
		if (!org.apache.commons.lang3.StringUtils.isEmpty(rs.getString("send_date"))) {
			result.setSendDate(format.parse(rs.getString("send_date")));
		}
		result.setComplexityName(rs.getString("complexity_name"));
		result.setDepthReport(rs.getString("depthReport"));
		return result;
	}

	/**
	 * Sets the search parameters.
	 *
	 * @param search             the search
	 * @param ps                 the ps
	 * @param nextParameterCount the next parameter count
	 * @throws SQLException   the SQL exception
	 * @throws ParseException the parse exception
	 */
	private static void setSearchParameters(final ServicioDiagnosticoForm search, PreparedStatement ps, int nextParameterCount) throws SQLException, ParseException {
		if (search != null) {
			final DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			if (!StringUtils.isEmpty(search.getUser())) {
				ps.setString(nextParameterCount++, "%" + search.getUser() + "%");
			}
			if (!StringUtils.isEmpty(search.getDomain())) {
				ps.setString(nextParameterCount++, "%" + search.getDomain() + "%");
			}
			if (!StringUtils.isEmpty(search.getEmail())) {
				ps.setString(nextParameterCount++, "%" + search.getEmail() + "%");
			}
			if (search.getDepth() != null) {
				ps.setInt(nextParameterCount++, search.getDepth());
			}
			if (search.getWidth() != null) {
				ps.setInt(nextParameterCount++, search.getWidth());
			}
			if (!StringUtils.isEmpty(search.getReport())) {
				ps.setString(nextParameterCount++, "%" + search.getReport() + "%");
			}
			if (!StringUtils.isEmpty(search.getStatus())) {
				ps.setString(nextParameterCount++, search.getStatus());
			}
			if (!StringUtils.isEmpty(search.getAnalysisType())) {
				ps.setString(nextParameterCount++, search.getAnalysisType());
			}
			if (search.getInDirectory() != null) {
				ps.setInt(nextParameterCount++, search.getInDirectory());
			}
			if (search.getStartDate() != null) {
				final java.util.Date startDate = format.parse(search.getStartDate());
				ps.setDate(nextParameterCount++, new Date(startDate.getTime()));
			}
			if (search.getEndDate() != null) {
				ps.setDate(nextParameterCount++, new Date(convertToIncludingEndDate(format, search.getEndDate()).getTime()));
			}
			if (!StringUtils.isEmpty(search.getDepthReport())) {
				ps.setString(nextParameterCount++, search.getDepthReport());
			}
		}
	}

	/**
	 * Adds the search parameters.
	 *
	 * @param search the search
	 * @param query  the query
	 */
	private static void addSearchParameters(final ServicioDiagnosticoForm search, StringBuilder query) {
		if (search != null) {
			if (!StringUtils.isEmpty(search.getUser())) {
				query.append(" AND usr LIKE ? ");
			}
			if (!StringUtils.isEmpty(search.getDomain())) {
				query.append(" AND domain LIKE ? ");
			}
			if (!StringUtils.isEmpty(search.getEmail())) {
				query.append(" AND email LIKE ? ");
			}
			if (search.getDepth() != null) {
				query.append(" AND depth = ? ");
			}
			if (search.getWidth() != null) {
				query.append(" AND width = ? ");
			}
			if (!StringUtils.isEmpty(search.getReport())) {
				query.append(" AND report LIKE ? ");
			}
			if (!StringUtils.isEmpty(search.getStatus())) {
				query.append(" AND status = ? ");
			}
			if (!StringUtils.isEmpty(search.getAnalysisType())) {
				query.append(" AND analysis_type = ? ");
			}
			if (search.getInDirectory() != null) {
				query.append(" AND 	in_directory = ? ");
			}
			if (search.getStartDate() != null) {
				query.append(" AND 	date >= STR_TO_DATE(?, '%Y-%m-%d') ");
			}
			if (search.getEndDate() != null) {
				query.append(" AND 	date <= STR_TO_DATE(?, '%Y-%m-%d') ");
			}
			if (search.getDepthReport() != null) {
				query.append(" AND 	depthReport LIKE ? ");
			}
		}
	}

	/**
	 * Convert to including end date.
	 *
	 * @param format  the format
	 * @param endDate the end date
	 * @return the java.util. date
	 * @throws ParseException the parse exception
	 */
	private static java.util.Date convertToIncludingEndDate(final DateFormat format, final String endDate) throws ParseException {
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(format.parse(endDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	private static String cleanUrls(String domain) {
		String[] urls = domain.trim().split("\n");
		List<String> out = new ArrayList<String>();
		for (String url : urls) {
			if (url.contains("http:") || url.contains("https:")) {
				out.add(url);
			}
		}
		// return Arrays.asList(out).stream().map(Object::toString).collect(Collectors.joining("\n"));
		return String.join("\n", out);
	}
}
