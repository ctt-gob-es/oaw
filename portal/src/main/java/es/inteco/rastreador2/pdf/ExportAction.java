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
package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.VerRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * The Class ExportAction.
 */
public class ExportAction extends Action {

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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute(Constants.ROLE) == null ||
                CrawlerUtils.hasAccess(request, "export.crawler.report")) {
            String crawlerType = request.getParameter(Constants.EXPORT_PDF_TYPE);
            return listPdf(mapping, form, request, response, crawlerType);
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }

    }

    /**
	 * List pdf.
	 *
	 * @param mapping     the mapping
	 * @param form        the form
	 * @param request     the request
	 * @param response    the response
	 * @param crawlerType the crawler type
	 * @return the action forward
	 */
    public ActionForward listPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, String crawlerType) {

        String user = (String) request.getSession().getAttribute(Constants.USER);

        long idTracking = 0;
        if (request.getParameter(Constants.ID) != null) {
            idTracking = Long.parseLong(request.getParameter(Constants.ID));
        }
        long idRastreo = 0;
        if (request.getParameter(Constants.ID_RASTREO) != null) {
            idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
        }

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        try (Connection c = DataBaseManager.getConnection()) {
            //Comprobamos que el usuario esta asociado con el rastreo que quiere exportar
            if (user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                if (crawlerType.equals(Constants.EXPORT_PDF_INTAV) || crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
                    return createIntavPdf(mapping, form, request, response, idRastreo, idTracking, language, crawlerType);
                } else if (crawlerType.equals(Constants.EXPORT_PDF_LENOX)) {
                    return mapping.findForward(Constants.ERROR);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        }
        return null;
    }

    /**
	 * Creates the intav pdf.
	 *
	 * @param mapping     the mapping
	 * @param form        the form
	 * @param request     the request
	 * @param response    the response
	 * @param idRastreo   the id rastreo
	 * @param idTracking  the id tracking
	 * @param language    the language
	 * @param crawlerType the crawler type
	 * @return the action forward
	 * @throws Exception the exception
	 */
    private ActionForward createIntavPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response, long idRastreo, long idTracking, Locale language, String crawlerType) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();

        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.intav")
                + idRastreo + File.separator + idTracking + File.separator + language.getLanguage();

        File checkFile = null;
        if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
            checkFile = new File(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.simple.file.intav.name"));
        } else {
            checkFile = new File(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.intav.name"));
        }

        List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idTracking);
        if (evaluationIds != null && !evaluationIds.isEmpty()) {
            // Si el pdf no ha sido creado lo creamos
            if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
                    IntavExport.exportIntavToSimplePdf(resultEvolutionData(idRastreo, idTracking), request, path, idTracking);
                } else {
                    IntavExport.exportIntavToPdf(resultEvolutionData(idRastreo, idTracking), request, path, idTracking);
                }

            }
        } else {
            if (request.getParameter("key") == null) {
                ActionErrors errors = new ActionErrors();
                errors.add("errorPDF", new ActionMessage("pdf.error.no.results"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.PDF_ERROR_FORWARD);
            } else {
                return null;
            }
        }

        if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
            CrawlerUtils.returnFile(response, path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.simple.file.intav.name"), "application/pdf", false);
        } else {
            CrawlerUtils.returnFile(response, path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.intav.name"), "application/pdf", false);
        }

        return null;
    }

    /**
	 * Result evolution data.
	 *
	 * @param idRastreo  the id rastreo
	 * @param idTracking the id tracking
	 * @return the map
	 * @throws Exception the exception
	 */
    public static Map<Long, List<Long>> resultEvolutionData(Long idRastreo, Long idTracking) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            VerRastreoForm vrf = new VerRastreoForm();
            vrf = RastreoDAO.cargarRastreoVer(c, idRastreo, vrf);
            final List<Long> trackingsIds = RastreoDAO.getEvolutionExecutedCrawlerIds(c, idRastreo, idTracking, vrf.getId_cartucho());
            final Map<Long, List<Long>> evaluationIdsMap = new HashMap<>();
            for (Long idTrack : trackingsIds) {
                List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idTrack);
                evaluationIdsMap.put(idTrack, evaluationIds);
            }

            return evaluationIdsMap;
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

}
