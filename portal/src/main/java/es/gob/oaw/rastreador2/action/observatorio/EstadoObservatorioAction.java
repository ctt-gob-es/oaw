/*******************************************************************************
* Copyright (C) 2018 Ministerio de Política Territorial y Función Pública, 
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
package es.gob.oaw.rastreador2.action.observatorio;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.crawler.dao.EstadoObservatorioDAO;
import es.inteco.crawler.job.ObservatoryStatus;
import es.inteco.crawler.job.ObservatorySummary;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;

/**
 * EstadoObservatorioAction. {@link Action} Estado de un observatorio.
 */
public class EstadoObservatorioAction extends Action {
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action. ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final Integer idObservatory = Integer.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		final Integer idEjecucionObservatorio = Integer.valueOf(request.getParameter(Constants.ID_EX_OBS));
		final Integer idCartucho = Integer.valueOf(request.getParameter(Constants.ID_CARTUCHO));
		try (Connection c = DataBaseManager.getConnection()) {
			ObservatorySummary estado = EstadoObservatorioDAO.getObservatorySummary(c, idObservatory, idEjecucionObservatorio);
			// Last
			ObservatoryStatus estadoObservatorio = EstadoObservatorioDAO.findEstadoObservatorio(c, idObservatory, idEjecucionObservatorio);
			// Not crawled yet
			List<SemillaForm> notCrawledSeedsYet = RastreoDAO.getFinishCrawlerFromSeedAndObservatoryNotCrawledYet(c, idObservatory.longValue(), idEjecucionObservatorio.longValue());
			// Not crawled (errors, etc)
			List<SemillaForm> notCrawledSeeds = RastreoDAO.getFinishCrawlerFromSeedAndObservatoryNotCrawled(c, idObservatory.longValue(), idEjecucionObservatorio.longValue());
			// Without results
			List<Long> finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis = ObservatorioDAO.getFinishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis(c, idObservatory.longValue(),
					idEjecucionObservatorio.longValue());
			List<ResultadoSemillaFullForm> finishWithoutResults = new ArrayList<ResultadoSemillaFullForm>();
			if (finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis != null && !finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis.isEmpty()) {
				finishWithoutResults = ObservatorioDAO.getResultSeedsFullFromObservatoryByIds(c, new SemillaForm(), idEjecucionObservatorio.longValue(), 0l, -1,
						finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis);
			}
			List<Long> lessThresholdIds = ObservatorioDAO.getFinishCrawlerIdsFromSeedAndObservatoryWithLessResultsThreshold(c, (long) idEjecucionObservatorio);
			List<ResultadoSemillaFullForm> finishLessThreshold = new ArrayList<ResultadoSemillaFullForm>();
			if (lessThresholdIds != null && !lessThresholdIds.isEmpty()) {
				finishLessThreshold = ObservatorioDAO.getResultSeedsFullFromObservatoryByIds(c, new SemillaForm(), idEjecucionObservatorio.longValue(), 0l, -1, lessThresholdIds);
			}
			request.setAttribute("idCartucho", idCartucho);
			request.setAttribute("idObservatory", idObservatory);
			request.setAttribute("idExecutedObservatorio", idEjecucionObservatorio);
			request.setAttribute("estado", estado);
			request.setAttribute("analisis", estadoObservatorio);
			request.setAttribute("notCrawledSeedsYet", notCrawledSeedsYet);
			request.setAttribute("notCrawledSeeds", notCrawledSeeds);
			request.setAttribute("finishWithoutResults", finishWithoutResults);
			request.setAttribute("finishLessThreshold", finishLessThreshold);
		}
		return mapping.findForward(Constants.EXITO);
	}
}
