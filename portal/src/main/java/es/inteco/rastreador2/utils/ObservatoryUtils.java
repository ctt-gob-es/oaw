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
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModificarObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.NuevoObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.utils.IntavUtils;

/**
 * The Class ObservatoryUtils.
 */
public final class ObservatoryUtils {
	/**
	 * Instantiates a new observatory utils.
	 */
	private ObservatoryUtils() {
	}

	/**
	 * Adds the seed to observatory.
	 *
	 * @param request      the request
	 * @param mapping      the mapping
	 * @param id_seed      the id seed
	 * @param fromAddSeeds the from add seeds
	 * @param addSeeds     the add seeds
	 * @param otherSeeds   the other seeds
	 * @return the action forward
	 */
	// Se vincula una semilla al observatorio
	public static ActionForward addSeedToObservatory(HttpServletRequest request, ActionMapping mapping, String id_seed, boolean fromAddSeeds, List<SemillaForm> addSeeds,
			List<SemillaForm> otherSeeds) {
		for (SemillaForm semillaForm : otherSeeds) {
			if (semillaForm.getId() == Long.parseLong(id_seed)) {
				otherSeeds.remove(semillaForm);
				addSeeds.add(semillaForm);
				break;
			}
		}
		return returnLists(request, mapping, addSeeds, otherSeeds, fromAddSeeds);
	}

	/**
	 * Separe seed to observatory.
	 *
	 * @param request      the request
	 * @param mapping      the mapping
	 * @param id_seed      the id seed
	 * @param fromAddSeeds the from add seeds
	 * @param addSeeds     the add seeds
	 * @param otherSeeds   the other seeds
	 * @return the action forward
	 */
	// Se desvincula una semilla al observatorio
	public static ActionForward separeSeedToObservatory(HttpServletRequest request, ActionMapping mapping, String id_seed, boolean fromAddSeeds, List<SemillaForm> addSeeds,
			List<SemillaForm> otherSeeds) {
		for (SemillaForm semillaForm : addSeeds) {
			if (semillaForm.getId() == Long.parseLong(id_seed)) {
				addSeeds.remove(semillaForm);
				otherSeeds.add(semillaForm);
				break;
			}
		}
		return returnLists(request, mapping, addSeeds, otherSeeds, fromAddSeeds);
	}

	// Redirecciona a nuevoObservatorio o añadirSemillas (dependiendo de dónde
	// venga) con los nuevos
	// parámetros de paginación (número de página de listado de semillas, lista
	/**
	 * Return lists.
	 *
	 * @param request      the request
	 * @param mapping      the mapping
	 * @param addSeeds     the add seeds
	 * @param otherSeeds   the other seeds
	 * @param fromAddSeeds the from add seeds
	 * @return the action forward
	 */
	// de enlaces... )
	public static ActionForward returnLists(HttpServletRequest request, ActionMapping mapping, List<SemillaForm> addSeeds, List<SemillaForm> otherSeeds, boolean fromAddSeeds) {
		int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
		int paginaNA = Pagination.getPage(request, Constants.PAG_PARAM2);
		PropertiesManager pmgr = new PropertiesManager();
		int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatorySeed.pagination.size"));
		request.getSession().setAttribute(Constants.ADD_OBSERVATORY_SEED_LIST, addSeeds);
		request.getSession().setAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST, otherSeeds);
		if (!fromAddSeeds) {
			int pagSizeNU = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "observatory.results.size"));
			int resultFrom = pagSizeNU * (pagina - 1);
			request.getSession().setAttribute(Constants.OBS_PAGINATION_RESULT_FROM, resultFrom);
			request.getSession().setAttribute(Constants.OBS_PAGINATION, pagSizeNU);
			request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, addSeeds.size(), String.valueOf(pagSizeNU), pagina, Constants.PAG_PARAM));
			return mapping.findForward(Constants.VOLVER);
		} else {
			int resultFrom = pagSize * (pagina - 1);
			int resultFromNA = pagSize * (paginaNA - 1);
			request.getSession().setAttribute(Constants.OBS_PAGINATION_RESULT_FROM, resultFrom);
			request.getSession().setAttribute(Constants.OBS_PAGINATION_RESULTNA_FROM, resultFromNA);
			request.getSession().setAttribute(Constants.OBS_PAGINATION, pagSize);
			request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, addSeeds.size(), String.valueOf(pagSize), pagina, Constants.PAG_PARAM));
			request.setAttribute(Constants.LIST_PAGE_LINKS2, Pagination.createPagination(request, otherSeeds.size(), String.valueOf(pagSize), paginaNA, Constants.PAG_PARAM2));
			return mapping.findForward(Constants.CARGAR_SEMILLAS_OBSERVATORIO);
		}
	}

	/**
	 * Removes the session attributes.
	 *
	 * @param request the request
	 */
	// Se borran los atributos de session
	public static void removeSessionAttributes(HttpServletRequest request) {
		request.getSession().removeAttribute(Constants.NUEVO_OBSERVATORIO_FORM);
		request.getSession().removeAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
		request.getSession().removeAttribute(Constants.ADD_OBSERVATORY_SEED_LIST);
		request.getSession().removeAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST);
	}

	/**
	 * Put session attributes.
	 *
	 * @param request                   the request
	 * @param modificarObservatorioForm the modificar observatorio form
	 */
	// Se incluyen los atributos en la session
	public static void putSessionAttributes(HttpServletRequest request, ModificarObservatorioForm modificarObservatorioForm) {
		request.getSession().setAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM, modificarObservatorioForm);
		request.getSession().setAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST, modificarObservatorioForm.getSemillasNoAnadidas());
		request.getSession().setAttribute(Constants.ADD_OBSERVATORY_SEED_LIST, modificarObservatorioForm.getSemillasAnadidas());
	}

	/**
	 * Put session attributes.
	 *
	 * @param request               the request
	 * @param nuevoObservatorioForm the nuevo observatorio form
	 */
	// Se incluyen los atributos en la session
	public static void putSessionAttributes(HttpServletRequest request, NuevoObservatorioForm nuevoObservatorioForm) {
		request.getSession().setAttribute(Constants.NUEVO_OBSERVATORIO_FORM, nuevoObservatorioForm);
		request.getSession().setAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST, nuevoObservatorioForm.getOtherSeeds());
		request.getSession().setAttribute(Constants.ADD_OBSERVATORY_SEED_LIST, new ArrayList<SemillaForm>());
	}

	/**
	 * Page suitability level.
	 *
	 * @param observatoryEvaluationForm the observatory evaluation form
	 * @return the string
	 */
	// Calcula el nivel de adecuacion de la pagina de un portal
	public static String pageSuitabilityLevel(ObservatoryEvaluationForm observatoryEvaluationForm) {
		PropertiesManager pmgr = new PropertiesManager();
		int maxFails = 0;
		String aplicacion = "";
		// Recuperamos el cartucho asociado al analsis
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			aplicacion = CartuchoDAO.getApplicationFromAnalisisId(c, observatoryEvaluationForm.getIdAnalysis());
			if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
			}
			DataBaseManager.closeConnection(c);
		} catch (Exception e) {
			Logger.putLog("Error with pageSuitabilityLevel calculation: observatory.zero.red.max.number key not found", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
		} finally {
			DataBaseManager.closeConnection(c);
		}
		if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
			final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
			// minhap.observatory.5_0.subgroup.3.4
			boolean declaracion = true;
			boolean situacionCumplimiento = true;
			int numZeroRed = 0;
			for (ObservatoryLevelForm observatoryLevel : observatoryEvaluationForm.getGroups()) {
				// Se recorren los niveles de adecuación
				for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevel.getSuitabilityGroups()) {
					if (observatorySuitabilityForm.getName().equals(Constants.OBS_A)) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
								if ("minhap.observatory.5_0.subgroup.3.1".equals(observatorySubgroupForm.getDescription())) {
									declaracion = false;
								}
								if ("minhap.observatory.5_0.subgroup.3.4".equals(observatorySubgroupForm.getDescription())) {
									situacionCumplimiento = false;
								}
								numZeroRed++;
							}
						}
					}
				}
			}
			if (numZeroRed == 0) {
				return Constants.OBS_ACCESIBILITY_FULL;
			} else if (declaracion && situacionCumplimiento) {
				return Constants.OBS_ACCESIBILITY_PARTIAL;
			} else if (!situacionCumplimiento) {
				return Constants.OBS_ACCESIBILITY_NONE;
			} else {
				return Constants.OBS_ACCESIBILITY_NA;
			}
		} else {
			boolean isA = true;
			boolean isAA = true;
			// Se recorren los niveles de análisis
			for (ObservatoryLevelForm observatoryLevel : observatoryEvaluationForm.getGroups()) {
				// Se recorren los niveles de adecuación
				for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevel.getSuitabilityGroups()) {
					int numZeroRed = 0;
					if (observatorySuitabilityForm.getName().equals(Constants.OBS_A)) {
						if (observatoryLevel.getName().equals(Constants.OBS_N1) || isA) {
							for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
									numZeroRed = numZeroRed + 1;
								}
							}
							if (numZeroRed > maxFails) {
								isA = false;
							}
						}
					} else if (observatorySuitabilityForm.getName().equals(Constants.OBS_AA) && isA) {
						if (observatoryLevel.getName().equals(Constants.OBS_N1) || isAA) {
							for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
									numZeroRed = numZeroRed + 1;
								}
							}
							if (numZeroRed > maxFails) {
								isAA = false;
							}
						}
					}
				}
			}
			if (isA && isAA) {
				return Constants.OBS_AA;
			} else if (isA) {
				return Constants.OBS_A;
			} else {
				return Constants.OBS_NV;
			}
		}
	}

	/**
	 * Gets the validation level.
	 *
	 * @param messageResources the message resources
	 * @param level            the level
	 * @return the validation level
	 */
	public static String getValidationLevel(final MessageResources messageResources, final String level) {
		if (Constants.OBS_AA.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.aa");
		} else if (Constants.OBS_A.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.a");
		} else if (Constants.OBS_NV.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.nv");
		} else if (Constants.OBS_ACCESIBILITY_FULL.equals(level)) {
			return "Completo";
		} else if (Constants.OBS_ACCESIBILITY_PARTIAL.equals(level)) {
			return "Parcial";
		} else if (Constants.OBS_ACCESIBILITY_NONE.equals(level)) {
			return "No válido";
		} else if (Constants.OBS_ACCESIBILITY_NA.equals(level)) {
			return "Sin declaracón";
		} else {
			return "";
		}
	}

	/**
	 * Sets the avg score.
	 *
	 * @param c                      the c
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ResultadoSemillaForm> setAvgScore(final Connection c, final List<ResultadoSemillaForm> seedsResults, final Long idFulfilledObservatory) throws Exception {
		final List<ObservatoryEvaluationForm> observatories = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idFulfilledObservatory), Constants.COMPLEXITY_SEGMENT_NONE,
				null);
		final Map<Long, List<FulFilledCrawling>> fullfilledCrawlings = RastreoDAO.getFulfilledCrawlings(c, seedsResults, idFulfilledObservatory);
		for (ResultadoSemillaForm seedResult : seedsResults) {
			final List<FulFilledCrawling> seedFulfilledCrawlings = fullfilledCrawlings.get(Long.valueOf(seedResult.getIdCrawling()));
			if (seedFulfilledCrawlings != null && !seedFulfilledCrawlings.isEmpty()) {
				int numPages = 0;
				BigDecimal avgScore = BigDecimal.ZERO;
				// Cambio de numero de urls maximas a anilizar
				PropertiesManager pmgr = new PropertiesManager();
				int maxUrl = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				final List<ObservatoryEvaluationForm> paginas = new ArrayList<>(maxUrl);
				for (ObservatoryEvaluationForm observatory : observatories) {
					if (observatory.getCrawlerExecutionId() == seedFulfilledCrawlings.get(0).getId()) {
						numPages++;
						avgScore = avgScore.add(observatory.getScore());
						paginas.add(observatory);
					}
				}
				if (numPages != 0) {
					seedResult.setScore(avgScore.divide(BigDecimal.valueOf(numPages), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
					String aplicacion = CartuchoDAO.getApplicationFromExecutedObservatoryId(c, Long.parseLong(seedResult.getIdFulfilledCrawling()), Long.parseLong(seedResult.getIdCrawling()));
					if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), paginas).getLevel());
					} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019), paginas).getLevel());
					} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), paginas).getLevel());
					} else {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources("ApplicationResources"), paginas).getLevel());
					}
				}
			}
		}
		return seedsResults;
	}

	/**
	 * Sets the avg score 2.
	 *
	 * @param c                      the c
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ResultadoSemillaFullForm> setAvgScore2(final Connection c, final List<ResultadoSemillaFullForm> seedsResults, final Long idFulfilledObservatory) throws Exception {
		final List<ObservatoryEvaluationForm> observatories = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idFulfilledObservatory), Constants.COMPLEXITY_SEGMENT_NONE,
				null);
		final Map<Long, List<FulFilledCrawling>> fullfilledCrawlings = RastreoDAO.getFulfilledCrawlings2(c, seedsResults, idFulfilledObservatory);
		for (ResultadoSemillaFullForm seedResult : seedsResults) {
			final List<FulFilledCrawling> seedFulfilledCrawlings = fullfilledCrawlings.get(Long.valueOf(seedResult.getIdCrawling()));
			if (seedFulfilledCrawlings != null && !seedFulfilledCrawlings.isEmpty()) {
				int numPages = 0;
				BigDecimal avgScore = BigDecimal.ZERO;
				// Cambio de numero de urls maximas a anilizar
				PropertiesManager pmgr = new PropertiesManager();
				int maxUrl = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				final List<ObservatoryEvaluationForm> paginas = new ArrayList<>(maxUrl);
				for (ObservatoryEvaluationForm observatory : observatories) {
					if (observatory.getCrawlerExecutionId() == seedFulfilledCrawlings.get(0).getId()) {
						numPages++;
						avgScore = avgScore.add(observatory.getScore());
						paginas.add(observatory);
					}
				}
				if (numPages != 0) {
					seedResult.setScore(avgScore.divide(BigDecimal.valueOf(numPages), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
					String aplicacion = CartuchoDAO.getApplicationFromExecutedObservatoryId(c, Long.parseLong(seedResult.getIdFulfilledCrawling()), Long.parseLong(seedResult.getIdCrawling()));
					if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScoresAccesibility(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), paginas).getLevel());
					} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScores2(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019), paginas).getLevel());
					} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), paginas).getLevel());
					} else {
						seedResult.setNivel(IntavUtils.generateScores(PropertyMessageResources.getMessageResources("ApplicationResources"), paginas).getLevel());
					}
					// Save scrore and level on database
					RastreoDAO.setScoreAndLevelCrawling(c, Long.valueOf(seedResult.getIdFulfilledCrawling()), seedResult.getScore(), seedResult.getNivel());
				}
			}
		}
		return seedsResults;
	}

	/**
	 * Set compliance level.
	 *
	 * @param c                      the c
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ResultadoSemillaForm> setCompliance(final Connection c, final List<ResultadoSemillaForm> seedsResults, final Long idFulfilledObservatory) throws Exception {
		final List<ObservatoryEvaluationForm> observatories = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idFulfilledObservatory), Constants.COMPLEXITY_SEGMENT_NONE,
				null);
		// IdExecutedCrawl and verifications
		// final Map<String, BigDecimal> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(observatories, Constants.OBS_PRIORITY_NONE);
		final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(observatories, Constants.OBS_PRIORITY_NONE);
		Map<Long, String> calculatedCompliance = calculateCrawlingCompliance(results);
		for (ResultadoSemillaForm seedResult : seedsResults) {
			seedResult.setCompliance(calculatedCompliance.get(Long.parseLong(seedResult.getIdFulfilledCrawling())));
		}
		return seedsResults;
	}

	/**
	 * Calculate crawling compliance.
	 *
	 * @param results the results
	 * @return the map
	 */
	private static Map<Long, String> calculateCrawlingCompliance(Map<Long, Map<String, BigDecimal>> results) {
		final Map<Long, String> resultCompilance = new TreeMap<>();
		// Process results
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			int countC = 0;
			int countNC = 0;
			int countNA = 0;
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					countC++;
				} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
					countNC++;
				} else {
					countNA++;
				}
			}
			if ((countC + countNA) == result.getValue().size()) {
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_FULL);
			} else if ((countC + countNA) > countNC) {
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_PARTIAL);
			} else {
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_NONE);
			}
		}
		return resultCompilance;
	}

	/**
	 * Calculate score and leve.
	 *
	 * @param c                      the c
	 * @param seedsResults           the seeds results
	 * @param idFulfilledObservatory the id fulfilled observatory
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ResultadoSemillaFullForm> setAvgScoreFullfilledCrawl(final Connection c, final List<ResultadoSemillaFullForm> seedsResults, final Long idFulfilledObservatory) throws Exception {
		final List<ObservatoryEvaluationForm> observatories = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idFulfilledObservatory), Constants.COMPLEXITY_SEGMENT_NONE,
				null);
		final Map<Long, List<FulFilledCrawling>> fullfilledCrawlings = RastreoDAO.getFulfilledCrawlings2(c, seedsResults, idFulfilledObservatory);
		for (ResultadoSemillaFullForm seedResult : seedsResults) {
			final List<FulFilledCrawling> seedFulfilledCrawlings = fullfilledCrawlings.get(Long.valueOf(seedResult.getIdCrawling()));
			if (seedFulfilledCrawlings != null && !seedFulfilledCrawlings.isEmpty()) {
				int numPages = 0;
				BigDecimal avgScore = BigDecimal.ZERO;
				// Cambio de numero de urls maximas a anilizar
				PropertiesManager pmgr = new PropertiesManager();
				int maxUrl = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				final List<ObservatoryEvaluationForm> paginas = new ArrayList<>(maxUrl);
				for (ObservatoryEvaluationForm observatory : observatories) {
					if (observatory.getCrawlerExecutionId() == seedFulfilledCrawlings.get(0).getId()) {
						numPages++;
						avgScore = avgScore.add(observatory.getScore());
						paginas.add(observatory);
					}
				}
				if (numPages != 0) {
					seedResult.setScore(avgScore.divide(BigDecimal.valueOf(numPages), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
					seedResult.setNivel(IntavUtils.generateScores(MessageResources.getMessageResources("ApplicationResources"), paginas).getLevel());
				}
			}
		}
		return seedsResults;
	}
}
