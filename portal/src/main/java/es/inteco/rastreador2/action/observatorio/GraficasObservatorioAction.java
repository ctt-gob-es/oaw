package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
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
            final PropertiesManager pmgr = new PropertiesManager();
            final Connection c = DataBaseManager.getConnection();
            try {
                if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(request.getParameter(Constants.TYPE_OBSERVATORY)))) {
                    return getIntavGraphic(request, response);
                } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
                    return getLenoxGraphic(request, response);
                } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
                    return getMultilanguageGraphic(request, response);
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward getIntavGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String graphic = request.getParameter(Constants.GRAPHIC);
        String graphicType = request.getParameter(Constants.GRAPHIC_TYPE);
        String execution_id = request.getParameter(Constants.ID);
        String observatory_id = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files") + File.separator + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
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
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.accessibility.level.allocation.segment.name", graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MARK_ALLOCATION_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.mark.allocation.segment.name", graphicType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name") + graphicType;
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name") + graphicType;
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.1.name") + graphicType;
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2_S)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.modality.by.verification.level.2.name") + graphicType;
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
                CrawlerUtils.returnFile(path + title + request.getParameter(Constants.OBSERVATORY_NUM_GRAPH) + ".jpg", response, "image/jpeg", false);
            } else {
                CrawlerUtils.returnFile(path + title + ".jpg", response, "image/jpeg", false);
            }
        }

        return null;
    }

    private ActionForward getLenoxGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        String execution_id = request.getParameter(Constants.ID);
        String observatory_id = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.lenox.files") + File.separator + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
            String title = "";
            if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_NUMBER_SEG_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.number.priority.segment.terms.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.percentage.priority.segment.terms.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG1)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.1seg") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.s1.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG2)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.2seg") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.s2.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG3)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.3seg") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.s3.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_HIGH_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.evolution.high.term.priority.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_MEDIUM_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.evolution.medium.term.priority.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_LOW_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.evolution.low.term.priority.name");
            } else if (graphic.equals(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_PERCENTAGE_TERMS)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.lenox.graphic.evolution.percentage.priority.terms.name");
            }

            CrawlerUtils.returnFile(path + title + ".jpg", response, "image/jpeg", false);
        }
        return null;
    }

    private ActionForward getMultilanguageGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        String graphicType = request.getParameter(Constants.GRAPHIC_TYPE);
        String execution_id = request.getParameter(Constants.ID);
        String observatory_id = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.multilanguage.files") + File.separator + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
            String title = "";

            if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_CORRECT_LINK_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_CORRECT_LINK_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_TR_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_TR_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_DEC_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_GLOBAL_PERCENTAGE_STACK_DEC_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_CORRECT_LINK_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_CORRECT_LINK_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.correct.link.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_TR_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_TR_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation.home.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_DEC_INTERNAL)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.inner.filename");
            } else if (graphic.equals(Constants.OBSERVATORY_MULTILANGUAGE_GRAPHIC_SEGMENT_PERCENTAGE_STACK_DEC_HOME)) {
                path += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphicType + File.separator;
                title = getResources(request).getMessage(getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration.home.filename");
            }

            CrawlerUtils.returnFile(path + title + ".jpg", response, "image/jpeg", false);
        }

        return null;
    }

}
