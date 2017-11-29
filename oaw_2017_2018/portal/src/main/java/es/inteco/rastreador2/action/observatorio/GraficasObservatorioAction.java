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
package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.Locale;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class GraficasObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
            try (Connection c = DataBaseManager.getConnection()){
                if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(request.getParameter(Constants.TYPE_OBSERVATORY)))) {
                    return getIntavGraphic(request, response);
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward getIntavGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        String graphicType = request.getParameter(Constants.GRAPHIC_TYPE);
        String executionId = request.getParameter(Constants.ID);
        String observatoryId = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            final PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files") + File.separator + observatoryId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
            String title = "";
            if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_GLOBAL_ALLOCATION)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accessibility.level.allocation.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_SEGMENTS_MARK)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.global.puntuation.allocation.segments.mark.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_GROUP_SEGMENT_MARK)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.global.puntuation.allocation.segment.strached.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accessibility.level.allocation.segment.name", getSegmentName(graphicType));
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MARK_ALLOCATION_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.mark.allocation.segment.name", getSegmentName(graphicType));
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name") + getSegmentName(graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name") + getSegmentName(graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.1.name") + getSegmentName(graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.2.name") + getSegmentName(graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_ASPECT)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.aspect.mid.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_A)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accesibility.evolution.approval.A.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_AA)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accesibility.evolution.approval.AA.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_NV)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accesibility.evolution.approval.NV.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_MID_MARK)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.evolution.mid.puntuation.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.1.name");
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.2.name");
            } else {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                if (graphicType.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_VERIFICATION)) {
                    title = getResources(request).getMessage(getLocale(request), "observatory.graphic.evolution.verification.mid.puntuation.name", graphic);
                } else if (graphicType.equals(Constants.GRAPHIC_ASPECT)) {
                    title = getResources(request).getMessage(getLocale(request), "observatory.graphic.evolution.aspect.mid.puntuation.name", graphic);
                }
            }
            if (request.getParameter(Constants.OBSERVATORY_NUM_GRAPH) != null) {
                CrawlerUtils.returnFile(response, path + title + request.getParameter(Constants.OBSERVATORY_NUM_GRAPH) + ".jpg", "image/jpeg", false);
            } else {
                CrawlerUtils.returnFile(response, path + title + ".jpg", "image/jpeg", false);
            }
        }

        return null;
    }

    private String getSegmentName(final String graphicType) {
        try (Connection c = DataBaseManager.getConnection()) {
            final CategoriaForm category = ObservatorioDAO.getCategoryById(c, Long.parseLong(graphicType));
            return String.valueOf(category.getOrden());
        } catch (Exception e) {
            Logger.putLog("Problema al generar las gráficas para el segmento " + graphicType, GraficasObservatorioAction.class, Logger.LOG_LEVEL_WARNING, e);
        }
        return graphicType;
    }

}
