package es.inteco.rastreador2.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Section;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public final class AnonymousResultExportPdfSectionEv {

    private AnonymousResultExportPdfSectionEv() {
    }

    //**************************************************************************************************
    //RESULTADOS: EVOLUCIÓN
    //**************************************************************************************************

    protected static void createChapterEvolution(final MessageResources messageResources, Chapter chapter) {
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev.p1"), ConstantsFont.PARAGRAPH, chapter);
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev.p2"), ConstantsFont.PARAGRAPH, chapter);
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev.p3"), ConstantsFont.PARAGRAPH, chapter);
    }

    protected static void createSectionEv1(HttpServletRequest request, Section section, String graphicPath, Map<String, BigDecimal> resultData) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev1.img.alt"), 80);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev1.ob.tableTitle"), section, 420);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.date"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.portales"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected static void createSectionEv2(HttpServletRequest request, Section section, String graphicPath, Map<String, BigDecimal> resultData) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev2.img.alt"), 80);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev2.ob.tableTitle"), section, 420);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.date"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.portales"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected static void createSectionEv3(HttpServletRequest request, Section section, String graphicPath, Map<String, BigDecimal> resultData) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev3.img.alt"), 80);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev3.ob.tableTitle"), section, 420);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.date"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.portales"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected static void createSectionEv4(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev4.img.alt"), 80);

        Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateEvolutionPuntuationDataSet(pageObservatoryMap);
        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev4.ob.tableTitle"), section, 420);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.date"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected static void createSectionEv5(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev5.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev5.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION);
    }

    protected static void createSectionEv6(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev6.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev6.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION);
    }

    protected static void createSectionEv7(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev7.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev7.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION);
    }

    protected static void createSectionEv8(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev8.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev8.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION);
    }

    protected static void createSectionEv9(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev9.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev9.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION);
    }

    protected static void createSectionEv10(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev10.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev10.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION);
    }

    protected static void createSectionEv11(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev11.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev11.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION);
    }

    protected static void createSectionEv12(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev12.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev12.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION);
    }

    protected static void createSectionEv13(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev13.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev13.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION);
    }

    protected static void createSectionEv14(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev14.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev14.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION);
    }

    protected static void createSectionEv15(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev15.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev15.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION);
    }

    protected static void createSectionEv16(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev16.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev16.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION);
    }

    protected static void createSectionEv17(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev17.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev17.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION);
    }

    protected static void createSectionEv18(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev18.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev18.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION);
    }

    protected static void createSectionEv19(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev19.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev19.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION);
    }

    protected static void createSectionEv20(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev20.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev20.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION);
    }

    protected static void createSectionEv21(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev21.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev21.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION);
    }

    protected static void createSectionEv22(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev22.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev22.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION);
    }

    protected static void createSectionEv23(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev23.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev23.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION);
    }

    protected static void createSectionEv24(HttpServletRequest request, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev24.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev24.ob.tableTitle"), section, 420);
        createVerificationResults(CrawlerUtils.getResources(request), section, pageObservatoryMap, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION);
    }

    protected static void createSectionEv25(HttpServletRequest request, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev25.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev25.ob.tableTitle")), section, 420);
        createAspectResults(CrawlerUtils.getResources(request), section, resultsByAspect, Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID);
    }

    protected static void createSectionEv26(HttpServletRequest request, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev26.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev26.ob.tableTitle")), section, 420);
        createAspectResults(CrawlerUtils.getResources(request), section, resultsByAspect, Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID);
    }

    protected static void createSectionEv27(HttpServletRequest request, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev27.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev27.ob.tableTitle")), section, 420);
        createAspectResults(CrawlerUtils.getResources(request), section, resultsByAspect, Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID);
    }

    protected static void createSectionEv28(HttpServletRequest request, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev28.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev28.ob.tableTitle")), section, 420);
        createAspectResults(CrawlerUtils.getResources(request), section, resultsByAspect, Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID);
    }

    protected static void createSectionEv29(HttpServletRequest request, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID) + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev29.img.alt"), 80);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Ev29.ob.tableTitle")), section, 420);
        createAspectResults(CrawlerUtils.getResources(request), section, resultsByAspect, Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID);
    }

    private static void createVerificationResults(final MessageResources messageResources, Section section, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap, String verification) {
        final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(verification, pageObservatoryMap);
        final java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData);
        final java.util.List<String> headers = new ArrayList<>();
        headers.add(messageResources.getMessage("resultados.anonimos.date"));
        headers.add(messageResources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    private static void createAspectResults(final MessageResources messageResources, Section section, Map<Date, Map<String, BigDecimal>> resultsByAspect, String aspect) {
        final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(aspect, resultsByAspect);
        final java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData);
        final java.util.List<String> headers = new ArrayList<>();
        headers.add(messageResources.getMessage("resultados.anonimos.date"));
        headers.add(messageResources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

}
