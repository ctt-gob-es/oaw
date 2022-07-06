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
package es.inteco.rastreador2.action.rastreo;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.LabelValueBean;
import org.jfree.data.category.DefaultCategoryDataset;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.EvaluationForm;
import es.inteco.intav.form.PriorityForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicsUtils;

/**
 * The Class ChartIntavAction.
 */
public class ChartIntavAction extends Action {
	/** The pmgr. */
	private static PropertiesManager pmgr = null;
	/** The x. */
	private static int x = 700;
	/** The y. */
	private static int y = 500;
	static {
		pmgr = new PropertiesManager();
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
	}

	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String chartsPath = getChartsPath(request);
			if (CrawlerUtils.hasAccess(request, "show.crawler.graphics")) {
				if (request.getParameter(Constants.GET_REGENERATE) != null && request.getParameter(Constants.GET_REGENERATE).equals("true")) {
					return createCharts(request, mapping, chartsPath, true);
				} else {
					return createCharts(request, mapping, chartsPath, false);
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, this.getClass());
			return mapping.findForward(Constants.ERROR_PAGE);
		}
	}

	/**
	 * Creates the charts.
	 *
	 * @param request    the request
	 * @param mapping    the mapping
	 * @param chartsPath the charts path
	 * @param regenerate the regenerate
	 * @return the action forward
	 */
	private ActionForward createCharts(HttpServletRequest request, ActionMapping mapping, String chartsPath, boolean regenerate) {
		String user = (String) request.getSession().getAttribute(Constants.USER);
		long idExecution = request.getParameter(Constants.ID) != null ? Long.parseLong(request.getParameter(Constants.ID)) : 0;
		long idRastreo = request.getParameter(Constants.ID_RASTREO) != null ? Long.parseLong(request.getParameter(Constants.ID_RASTREO)) : 0;
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			// Comprobamos que el usuario esta asociado con el rastreo de las graficas que quiere recuperar
			if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
				int have_results = createChart(idExecution, chartsPath, request, regenerate);
				if (have_results == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
					ActionErrors errors = new ActionErrors();
					errors.add("graficaObservatorio", new ActionMessage("observatorio.no.resultados"));
					saveErrors(request, errors);
					request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
				} else {
					request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ChartIntavAction.class, Logger.LOG_LEVEL_ERROR, e);
			return mapping.findForward(Constants.ERROR);
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return mapping.findForward(Constants.INDICE_GRAFICAS);
	}

	/**
	 * Creates the chart.
	 *
	 * @param idExecution the id execution
	 * @param chartsPath  the charts path
	 * @param request     the request
	 * @param regenerate  the regenerate
	 * @return the int
	 * @throws Exception the exception
	 */
	private int createChart(Long idExecution, String chartsPath, HttpServletRequest request, boolean regenerate) throws Exception {
		List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idExecution);
		Map<String, Map<String, BigDecimal>> globalDataMap = new HashMap<>();
		Map<String, BigDecimal> totalDataMap = new HashMap<>();
		// Inicializamos el evaluador
		EvaluatorUtility.initialize();
		Connection conn = null;
		try {
			conn = DataBaseManager.getConnection();
			int numTotalProblems = 0;
			int numTotalWarnings = 0;
			int numTotalInfos = 0;
			if (evaluationIds.size() != 0) {
				for (Long id : evaluationIds) {
					try {
						Evaluator evaluator = new Evaluator();
						Evaluation evaluation = evaluator.getAnalisisDB(conn, id, EvaluatorUtils.getDocList(), true);
						EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, EvaluatorUtility.getLanguage(request));
						for (PriorityForm priority : evaluationForm.getPriorities()) {
							EvaluatorUtils.countProblems(priority);
							HashMap<String, BigDecimal> dataMap = new HashMap<>();
							dataMap.put(getResources(request).getMessage(getLocale(request), "chart.problems"), new BigDecimal(priority.getNumProblems()));
							dataMap.put(getResources(request).getMessage(getLocale(request), "chart.warnings"), new BigDecimal(priority.getNumWarnings()));
							dataMap.put(getResources(request).getMessage(getLocale(request), "chart.infos"), new BigDecimal(priority.getNumInfos()));
							String priorityName = "";
							if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.1.name")))) {
								priorityName = getResources(request).getMessage(getLocale(request), "first.level.incidents");
							} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.2.name")))) {
								priorityName = getResources(request).getMessage(getLocale(request), "second.level.incidents");
							} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.3.name")))) {
								priorityName = getResources(request).getMessage(getLocale(request), "third.level.incidents");
							}
							if (globalDataMap.get(priorityName) == null) {
								globalDataMap.put(priorityName, dataMap);
							} else {
								HashMap<String, BigDecimal> auxDataMap = new HashMap<>();
								for (Map.Entry<String, BigDecimal> dataEntry : dataMap.entrySet()) {
									auxDataMap.put(dataEntry.getKey(), globalDataMap.get(priorityName).get(dataEntry.getKey()).add(dataEntry.getValue()));
								}
								globalDataMap.put(priorityName, auxDataMap);
							}
							if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.1.name")))
									|| priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.2.name")))) {
								numTotalProblems += priority.getNumProblems();
								numTotalWarnings += priority.getNumWarnings();
								numTotalInfos += priority.getNumInfos();
							}
						}
						totalDataMap.put(getResources(request).getMessage(getLocale(request), "chart.problems"), new BigDecimal(numTotalProblems));
						totalDataMap.put(getResources(request).getMessage(getLocale(request), "chart.warnings"), new BigDecimal(numTotalWarnings));
						totalDataMap.put(getResources(request).getMessage(getLocale(request), "chart.infos"), new BigDecimal(numTotalInfos));
					} catch (Exception e) {
						Logger.putLog("Error al crear una gráfica de resultados", ChartIntavAction.class, Logger.LOG_LEVEL_ERROR, e);
					}
				}
				generateCharts(request, globalDataMap, totalDataMap, chartsPath, regenerate);
				request.setAttribute(Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N1,
						labelInformation(globalDataMap.get(getResources(request).getMessage(getLocale(request), "first.level.incidents"))));
				request.setAttribute(Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N2,
						labelInformation(globalDataMap.get(getResources(request).getMessage(getLocale(request), "second.level.incidents"))));
				request.setAttribute(Constants.CRAWLER_GRAPHIC_TOTAL_RESULTS_LIST, labelInformation(totalDataMap));
				return Constants.OBSERVATORY_HAVE_RESULTS;
			} else {
				return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
			}
		} catch (Exception e) {
			Logger.putLog("Error al crear las gráficas de resultados", ChartIntavAction.class, Logger.LOG_LEVEL_ERROR, e);
		} finally {
			DataBaseManager.closeConnection(conn);
		}
		return 0;
	}

	/**
	 * Gets the charts path.
	 *
	 * @param request the request
	 * @return the charts path
	 */
	private String getChartsPath(HttpServletRequest request) {
		long idTracking = 0;
		if (request.getParameter(Constants.ID_RASTREO) != null) {
			idTracking = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
		}
		long idExecution = 0;
		if (request.getParameter(Constants.ID) != null) {
			idExecution = Long.parseLong(request.getParameter(Constants.ID));
		}
		Locale language = getLocale(request);
		if (language == null) {
			language = request.getLocale();
		}
		return pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + idTracking + File.separator + idExecution + File.separator + language.getLanguage()
				+ File.separator;
	}

	/**
	 * Generate charts.
	 *
	 * @param request       the request
	 * @param globalDataMap the global data map
	 * @param totalDataMap  the total data map
	 * @param chartsPath    the charts path
	 * @param regenerate    the regenerate
	 * @throws Exception the exception
	 */
	private void generateCharts(HttpServletRequest request, Map<String, Map<String, BigDecimal>> globalDataMap, Map<String, BigDecimal> totalDataMap, String chartsPath, boolean regenerate)
			throws Exception {
		DefaultCategoryDataset globalPriorityDataset = new DefaultCategoryDataset();
		DefaultCategoryDataset totalPriorityDataset = new DefaultCategoryDataset();
		String rTitle = CrawlerUtils.getResources(request).getMessage(getLocale(request), "chart.intav.rowTitle");
		String cTitle = CrawlerUtils.getResources(request).getMessage(getLocale(request), "chart.intav.columnTitle");
		String color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.intav.colors");
		for (Map.Entry<String, Map<String, BigDecimal>> globalDataEntry : globalDataMap.entrySet()) {
			for (Map.Entry<String, BigDecimal> dataEntry : globalDataEntry.getValue().entrySet()) {
				globalPriorityDataset.addValue(dataEntry.getValue(), dataEntry.getKey(), globalDataEntry.getKey());
			}
		}
		File f1 = new File(chartsPath + getResources(request).getMessage(getLocale(request), "chart.intav.priority.warnings") + ".jpg");
		if (regenerate || !f1.exists()) {
			ChartForm globalChart = new ChartForm(getResources(request).getMessage(getLocale(request), "chart.intav.priority.warnings"), cTitle, rTitle, globalPriorityDataset, true, true, false,
					false, true, false, false, x, y, color);
			GraphicsUtils.createSeriesBarChart(globalChart, chartsPath + getResources(request).getMessage(getLocale(request), "chart.intav.priority.warnings") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
		}
		for (Map.Entry<String, BigDecimal> totalDataEntry : totalDataMap.entrySet()) {
			totalPriorityDataset.addValue(totalDataEntry.getValue(), totalDataEntry.getKey(), "");
		}
		File f2 = new File(chartsPath + getResources(request).getMessage(getLocale(request), "chart.intav.total.results") + ".jpg");
		if (regenerate || !f2.exists()) {
			ChartForm totalChart = new ChartForm(getResources(request).getMessage(getLocale(request), "chart.intav.total.results"), cTitle, rTitle, totalPriorityDataset, true, false, false, false,
					true, false, false, x, y, color);
			GraphicsUtils.createStandardBarChart(totalChart, chartsPath + getResources(request).getMessage("chart.intav.total.results") + ".jpg", "", getResources(request), false);
		}
	}

	/**
	 * Label information.
	 *
	 * @param info the info
	 * @return the list
	 */
	private List<LabelValueBean> labelInformation(Map<String, BigDecimal> info) {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue;
		for (Map.Entry<String, BigDecimal> entry : info.entrySet()) {
			labelValue = new LabelValueBean();
			labelValue.setLabel(entry.getKey());
			labelValue.setValue(String.valueOf(entry.getValue()));
			labelValueList.add(labelValue);
		}
		return labelValueList;
	}
}
