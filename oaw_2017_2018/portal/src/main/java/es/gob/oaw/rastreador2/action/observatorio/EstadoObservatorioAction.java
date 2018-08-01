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

/**
 * EstadoObservatorioAction. {@link Action} Estado de un observatorio.
 *
 * @author alvaro.pelaez
 */
public class EstadoObservatorioAction extends Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.
	 * ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final Integer idObservatory = Integer.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		final Integer idEjecucionObservatorio = Integer.valueOf(request.getParameter(Constants.ID_EX_OBS));

		try (Connection c = DataBaseManager.getConnection()) {

			ObservatorySummary estado = EstadoObservatorioDAO.getObservatorySummary(c, idObservatory, idEjecucionObservatorio);
			
			
			//TODO Recuerar datos
//			estado.setEstado("En ejecución");
//
//			estado.setTotalSemillas(379);
//			estado.setSemillasAnalizadas(128);
//
//			estado.setPorcentajeCompletado(
//					((float) estado.getSemillasAnalizadas() / (float) estado.getTotalSemillas()) * 100);
//			estado.setTiempoMedio(17);
//			estado.setTiempoTotal(1897);
//			estado.setTiempoEstimado(
//					(estado.getTotalSemillas() - estado.getSemillasAnalizadas()) * estado.getTiempoMedio());
//			estado.setTiempoMinimo(
//					new ObservatorySummaryTimes(11, "http://www.defensa.gob.es", "Ministerio de Defensa"));
//			estado.setTiempoMaximo(
//					new ObservatorySummaryTimes(11, "http://www.mineco.gob.es", "Ministerio de Economía"));

			//Estado del último análisis hecho/análisis en curso
			ObservatoryStatus estadoObservatorio = EstadoObservatorioDAO.findEstadoObservatorio(c, idObservatory,
					idEjecucionObservatorio);

			// TODO Datos del observatorio
			request.setAttribute("estado", estado);
			request.setAttribute("analisis", estadoObservatorio);

		}

		return mapping.findForward(Constants.EXITO);

	}
}
