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
package es.inteco.intav.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.css.CSSResource;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.EvaluationForm;
import es.inteco.intav.persistence.Analysis;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;

/**
 * The Class AnalisisDatos.
 */
public final class AnalisisDatos {
	/**
	 * Instantiates a new analisis datos.
	 */
	private AnalisisDatos() {
	}

	/**
	 * Sets the analisis.
	 *
	 * @param connection   the connection
	 * @param analisis     the analisis
	 * @param cssResources the css resources
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int setAnalisis(final Connection connection, final Analysis analisis, final List<CSSResource> cssResources) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO tanalisis (fec_analisis, cod_url, num_duracion, nom_entidad, cod_rastreo, cod_guideline, estado, cod_fuente)" + " VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS)) {
			connection.setAutoCommit(false);
			pstmt.setString(1, analisis.getUrl());
			pstmt.setLong(2, analisis.getTime());
			pstmt.setString(3, analisis.getEntity());
			pstmt.setLong(4, analisis.getTracker());
			pstmt.setInt(5, getCodGuideline(connection, analisis.getGuideline()));
			pstmt.setInt(6, IntavConstants.STATUS_EXECUTING);
			// Encode BASE64 code
			String codigoFuente = analisis.getSource();
			if (!StringUtils.isEmpty(codigoFuente)) {
				codigoFuente = new String(Base64.encodeBase64(codigoFuente.getBytes("UTF-8")));
			}
			pstmt.setString(7, codigoFuente);
			pstmt.executeUpdate();
			final int codigoAnalisis;
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					codigoAnalisis = rs.getInt(1);
				} else {
					return 0;
				}
			}
			saveCSSResources(connection, codigoAnalisis, cssResources);
			connection.commit();
			connection.setAutoCommit(true);
			return codigoAnalisis;
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			connection.rollback();
			connection.setAutoCommit(true);
			return -1;
		}
	}

	/**
	 * Save CSS resources.
	 *
	 * @param connection     the connection
	 * @param codigoAnalisis the codigo analisis
	 * @param cssResources   the css resources
	 * @throws SQLException the SQL exception
	 */
	private static void saveCSSResources(final Connection connection, final int codigoAnalisis, final List<CSSResource> cssResources) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO tanalisis_css (url, codigo, cod_analisis) VALUES (?,?,?);")) {
			for (CSSResource cssResource : cssResources) {
				if (cssResource.isImported()) {
					pstmt.setString(1, cssResource.getStringSource());
					// Encode BASE64 code
					String codigoFuente = cssResource.getContent();
					if (!StringUtils.isEmpty(codigoFuente)) {
						codigoFuente = new String(Base64.encodeBase64(codigoFuente.getBytes()));
					}
					pstmt.setString(2, codigoFuente);
					pstmt.setInt(3, codigoAnalisis);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
		} catch (SQLException e) {
			Logger.putLog("Error en saveCSSResources", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Sets the analysis error.
	 *
	 * @param checkAccessibility the check accessibility
	 * @return the int
	 */
	public static int setAnalysisError(final CheckAccessibility checkAccessibility) {
		try (Connection conn = DataBaseManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO tanalisis (FEC_ANALISIS, COD_URL, NUM_DURACION, NOM_ENTIDAD, COD_RASTREO, COD_GUIDELINE, ESTADO)" + " VALUES (NOW(), ?, ?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, checkAccessibility.getUrl());
			pstmt.setLong(2, 0);
			pstmt.setString(3, checkAccessibility.getEntity());
			pstmt.setLong(4, checkAccessibility.getIdRastreo());
			pstmt.setInt(5, getCodGuideline(conn, checkAccessibility.getGuidelineFile()));
			pstmt.setInt(6, IntavConstants.STATUS_ERROR);
			pstmt.executeUpdate();
			int codigoAnalisis = 0;
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					codigoAnalisis = rs.getInt(1);
				}
			}
			return codigoAnalisis;
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return -1;
		}
	}

	/**
	 * Gets the cod guideline.
	 *
	 * @param connection the connection
	 * @param guideline  the guideline
	 * @return the cod guideline
	 * @throws SQLException the SQL exception
	 */
	private static int getCodGuideline(final Connection connection, final String guideline) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("SELECT cod_guideline FROM tguidelines WHERE des_guideline = ?;")) {
			pstmt.setString(1, getGuideline(guideline));
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("COD_GUIDELINE");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Update checks ejecutados.
	 *
	 * @param updatedChecks the updated checks
	 * @param idAnalisis    the id analisis
	 */
	public static void updateChecksEjecutados(final String updatedChecks, final long idAnalisis) {
		try (Connection conn = DataBaseManager.getConnection(); final PreparedStatement pstmt = conn.prepareStatement("UPDATE tanalisis SET CHECKS_EJECUTADOS = ? WHERE COD_ANALISIS = ?;")) {
			pstmt.setString(1, updatedChecks);
			pstmt.setLong(2, idAnalisis);
			pstmt.executeUpdate();
		} catch (Exception e) {
			Logger.putLog("updateChecksEjecutados: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * End analysis success.
	 *
	 * @param eval the eval
	 */
	public static void endAnalysisSuccess(final Evaluation eval) {
		try (Connection conn = DataBaseManager.getConnection();
				final PreparedStatement pstmt = conn.prepareStatement("UPDATE tanalisis SET CHECKS_EJECUTADOS = ?, ESTADO = ? WHERE COD_ANALISIS = ?;")) {
			pstmt.setString(1, eval.getChecksExecutedStr());
			pstmt.setInt(2, IntavConstants.STATUS_SUCCESS);
			pstmt.setLong(3, eval.getIdAnalisis());
			pstmt.executeUpdate();
		} catch (Exception e) {
			Logger.putLog("endAnalysisSuccess: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the analisis from id if is not an annexes generation
	 *
	 * @param conn the conn
	 * @param id   the id
	 * @return the analisis from id
	 */
	public static Analysis getAnalisisFromId(Connection conn, long id) {
		return getAnalisisFromId(conn, id, false);
	}

	/**
	 * Gets the analisis from id
	 *
	 * @param conn the conn
	 * @param id   the id
	 * @return the analisis from id
	 */
	public static Analysis getAnalisisFromId(Connection conn, long id, boolean originAnnexes) {
		final Analysis analisis = new Analysis();
		// Decode base 64 cod_fuente
//		try (PreparedStatement pstmt = conn.prepareStatement(
//				"SELECT COD_ANALISIS,FEC_ANALISIS,COD_URL,NOM_ENTIDAD,COD_RASTREO,DES_GUIDELINE,CHECKS_EJECUTADOS, FROM_BASE64(COD_FUENTE) as COD_FUENTE FROM tanalisis A INNER JOIN tguidelines G ON A.cod_guideline = G.cod_guideline "
//						+ "WHERE cod_analisis = ?;")) {
		String statement = "SELECT COD_ANALISIS, FEC_ANALISIS, COD_URL,NOM_ENTIDAD, COD_RASTREO, DES_GUIDELINE, CHECKS_EJECUTADOS, COD_FUENTE as COD_FUENTE FROM tanalisis A INNER JOIN tguidelines G ON A.cod_guideline = G.cod_guideline WHERE cod_analisis = ?;";
		if (originAnnexes) {
			statement = "SELECT COD_ANALISIS, FEC_ANALISIS, COD_URL, NOM_ENTIDAD, COD_RASTREO, DES_GUIDELINE, CHECKS_EJECUTADOS FROM tanalisis A INNER JOIN tguidelines G ON A.cod_guideline = G.cod_guideline WHERE cod_analisis = ?;";
		}
		try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
			pstmt.setLong(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					analisis.setCode(rs.getInt("COD_ANALISIS"));
					analisis.setDate(rs.getDate("FEC_ANALISIS"));
					analisis.setUrl(rs.getString("COD_URL"));
					analisis.setEntity(rs.getString("NOM_ENTIDAD"));
					analisis.setTracker(rs.getLong("COD_RASTREO"));
					analisis.setGuideline(rs.getString("DES_GUIDELINE"));
					analisis.setChecksExecutedStr(rs.getString("CHECKS_EJECUTADOS"));
					String source = "";
					if (!originAnnexes) {
						if (rs.getString("COD_FUENTE") != null) {
							source = new String(Base64.decodeBase64(rs.getString("COD_FUENTE").getBytes()));
						}
					}
					analisis.setSource(source);
				} else {
					return null;
				}
			}
			return analisis;
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return null;
		}
	}

	/**
	 * Gets the analysis by tracking.
	 *
	 * @param idTracking the id tracking
	 * @param pagina     the pagina
	 * @param request    the request
	 * @return the analysis by tracking
	 */
	public static List<Analysis> getAnalysisByTracking(long idTracking, int pagina, HttpServletRequest request, boolean originAnnexes) {
		final String query;
		if (pagina == IntavConstants.NO_PAGINATION) {
			query = "SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ?";
		} else {
			query = "SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ? ORDER BY FEC_ANALISIS ASC LIMIT ? OFFSET ?";
		}
		final PropertiesManager pmgr = new PropertiesManager();
		final int pagSize = Integer.parseInt(pmgr.getValue("intav.properties", "pagination.size"));
		final int resultFrom = pagSize * pagina;
		try (Connection conn = DataBaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setLong(1, idTracking);
			if (pagina != IntavConstants.NO_PAGINATION) {
				pstmt.setInt(2, pagSize);
				pstmt.setInt(3, resultFrom);
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				return getAnalysisList(conn, rs, EvaluatorUtility.getLanguage(request), originAnnexes);
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return null;
		}
	}

	/**
	 * Gets the analysis ids by tracking.
	 *
	 * @param conn       the conn
	 * @param idTracking the id tracking
	 * @return the analysis ids by tracking
	 */
	public static List<Long> getAnalysisIdsByTracking(final Connection conn, final long idTracking) {
		final List<Long> results = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis WHERE cod_rastreo = ? AND checks_ejecutados IS NOT NULL")) {
			pstmt.setLong(1, idTracking);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					results.add(rs.getLong("cod_analisis"));
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
		}
		return results;
	}

	/**
	 * Count analysis by tracking.
	 *
	 * @param idTracking the id tracking
	 * @return the int
	 */
	public static int countAnalysisByTracking(long idTracking) {
		try (Connection conn = DataBaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM tanalisis WHERE cod_rastreo = ?")) {
			pstmt.setLong(1, idTracking);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
		}
		return 0;
	}

	/**
	 * Gets the analysis list.
	 *
	 * @param conn     the conn
	 * @param rs       the rs
	 * @param language the language
	 * @return the analysis list
	 * @throws SQLException the SQL exception
	 */
	private static List<Analysis> getAnalysisList(final Connection conn, final ResultSet rs, final String language, final boolean originAnnexes) throws SQLException {
		final List<Analysis> listAnalysis = new ArrayList<>();
		while (rs.next()) {
			final Analysis analysis = new Analysis();
			analysis.setCode(rs.getInt("cod_analisis"));
			analysis.setDate(rs.getTimestamp("fec_analisis"));
			analysis.setUrl(rs.getString("cod_url"));
			analysis.setEntity(rs.getString("nom_entidad"));
			analysis.setTracker(rs.getInt("cod_rastreo"));
			analysis.setStatus(rs.getInt("estado"));
			if (analysis.getStatus() == IntavConstants.STATUS_SUCCESS) {
				final Evaluator evaluator = new Evaluator();
				try {
					final Evaluation evaluation = evaluator.getAnalisisDB(conn, analysis.getCode(), EvaluatorUtils.getDocList(), true, originAnnexes);
					final EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, language);
					for (int i = 0; i < evaluationForm.getPriorities().size(); i++) {
						analysis.setProblems(analysis.getProblems() + evaluationForm.getPriorities().get(i).getNumProblems());
						analysis.setWarnings(analysis.getWarnings() + evaluationForm.getPriorities().get(i).getNumWarnings());
						analysis.setObservations(analysis.getObservations() + evaluationForm.getPriorities().get(i).getNumInfos());
					}
				} catch (Exception e) {
					Logger.putLog("getAnalysisList: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			listAnalysis.add(analysis);
		}
		return listAnalysis;
	}

	/**
	 * Gets the evaluation ids from rastreo realizado.
	 *
	 * @param idRastreoRealizado the id rastreo realizado
	 * @return the evaluation ids from rastreo realizado
	 */
	public static List<Long> getEvaluationIdsFromRastreoRealizado(long idRastreoRealizado) {
		final List<Long> evaluationIds = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE cod_rastreo = ? ORDER by cod_analisis")) {
			pstmt.setLong(1, idRastreoRealizado);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					evaluationIds.add(rs.getLong(1));
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return evaluationIds;
		}
		return evaluationIds;
	}

	/**
	 * Gets the evaluation ids from executed observatory.
	 *
	 * @param idExObs the id ex obs
	 * @return the evaluation ids from executed observatory
	 */
	public static List<Long> getEvaluationIdsFromExecutedObservatory(long idExObs) {
		final List<Long> evaluationIds = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("SELECT distinct cod_analisis FROM tanalisis t WHERE cod_rastreo in (select id from rastreos_realizados where id_obs_realizado = ?) ORDER by cod_analisis")) {
			pstmt.setLong(1, idExObs);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					evaluationIds.add(rs.getLong(1));
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return evaluationIds;
		}
		return evaluationIds;
	}

	/**
	 * Gets the evaluation ids from executed observatory and id seed.
	 *
	 * @param idExObs the id ex obs
	 * @param idSeed  the id seed
	 * @return the evaluation ids from executed observatory and id seed
	 */
	public static List<Long> getEvaluationIdsFromExecutedObservatoryAndIdSeed(final Long idExObs, final Long idSeed) {
		final List<Long> evaluationIds = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"SELECT distinct cod_analisis FROM tanalisis t JOIN rastreos_realizados rr on t.cod_rastreo=rr.id WHERE cod_rastreo in (select id from rastreos_realizados where id_obs_realizado = ?) AND rr.id_lista=? ORDER by cod_analisis")) {
			pstmt.setLong(1, idExObs);
			pstmt.setLong(2, idSeed);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					evaluationIds.add(rs.getLong(1));
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
			return evaluationIds;
		}
		return evaluationIds;
	}

	/**
	 * Obtiene todos los recursos CSS (CSSDTO) que están asociados a una evaluación, análisis de una página.
	 *
	 * @param idCodAnalisis el identificador de la evaluación.
	 * @return una lista con todos los recursos CSS (CSSDTO) que se analizaron para esa evaluación.
	 */
	public static List<CSSDTO> getCSSResourcesFromEvaluation(final long idCodAnalisis) {
		final List<CSSDTO> evaluationIds = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection();
				// Decode codigo BASE64
				PreparedStatement pstmt = conn.prepareStatement("SELECT url, codigo AS codigo FROM tanalisis_css t WHERE cod_analisis = ?")) {
			pstmt.setLong(1, idCodAnalisis);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					evaluationIds.add(new CSSDTO(rs.getString(1), new String(Base64.decodeBase64(rs.getString(2).getBytes()))));
				}
			}
		} catch (Exception ex) {
			Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
		}
		return evaluationIds;
	}

	/**
	 * Gets the guideline.
	 *
	 * @param guideline the guideline
	 * @return the guideline
	 */
	private static String getGuideline(final String guideline) {
		if (guideline.contains("-nobroken")) {
			return guideline.replace("-nobroken", "");
		} else {
			return guideline;
		}
	}
}