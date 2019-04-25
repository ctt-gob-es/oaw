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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModificarObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.NuevoObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.rastreo.RastreoEjecutadoForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.utils.IntavUtils;

public final class ObservatoryUtils {

	private ObservatoryUtils() {
	}

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

	// Se borran los atributos de session
	public static void removeSessionAttributes(HttpServletRequest request) {
		request.getSession().removeAttribute(Constants.NUEVO_OBSERVATORIO_FORM);
		request.getSession().removeAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
		request.getSession().removeAttribute(Constants.ADD_OBSERVATORY_SEED_LIST);
		request.getSession().removeAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST);
	}

	// Se incluyen los atributos en la session
	public static void putSessionAttributes(HttpServletRequest request, ModificarObservatorioForm modificarObservatorioForm) {
		request.getSession().setAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM, modificarObservatorioForm);
		request.getSession().setAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST, modificarObservatorioForm.getSemillasNoAnadidas());
		request.getSession().setAttribute(Constants.ADD_OBSERVATORY_SEED_LIST, modificarObservatorioForm.getSemillasAnadidas());
	}

	// Se incluyen los atributos en la session
	public static void putSessionAttributes(HttpServletRequest request, NuevoObservatorioForm nuevoObservatorioForm) {
		request.getSession().setAttribute(Constants.NUEVO_OBSERVATORIO_FORM, nuevoObservatorioForm);
		request.getSession().setAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST, nuevoObservatorioForm.getOtherSeeds());
		request.getSession().setAttribute(Constants.ADD_OBSERVATORY_SEED_LIST, new ArrayList<SemillaForm>());
	}

	// Calcula el nivel de adecuacion de la pagina de un portal
	public static String pageSuitabilityLevel(ObservatoryEvaluationForm observatoryEvaluationForm) {
		PropertiesManager pmgr = new PropertiesManager();
		int maxFails = 0;

		// Recuperamos el cartucho asociado al analsis
		try (Connection c = DataBaseManager.getConnection()) {

			String aplicacion = CartuchoDAO.getApplicationFromAnalisisId(c, observatoryEvaluationForm.getIdAnalysis());

			if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
			}

			DataBaseManager.closeConnection(c);

		} catch (Exception e) {
			maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
		}

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

	public static String getValidationLevel(final MessageResources messageResources, final String level) {
		if (Constants.OBS_AA.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.aa");
		} else if (Constants.OBS_A.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.a");
		} else if (Constants.OBS_NV.equals(level)) {
			return messageResources.getMessage("resultados.anonimos.num.portales.nv");
		} else {
			return "";
		}
	}

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
					seedResult.setNivel(IntavUtils.generateScores(MessageResources.getMessageResources("ApplicationResources"), paginas).getLevel());
				}
			}
		}
		return seedsResults;
	}

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
					seedResult.setNivel(IntavUtils.generateScores(MessageResources.getMessageResources("ApplicationResources"), paginas).getLevel());
				}
			}
		}
		return seedsResults;
	}

}
