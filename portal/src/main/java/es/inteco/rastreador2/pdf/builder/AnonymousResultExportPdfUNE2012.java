package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static es.inteco.common.ConstantsFont.LINE_SPACE;

/**
 *
 */
public class AnonymousResultExportPdfUNE2012 extends AnonymousResultExportPdf {


    private final BasicServiceForm basicServiceForm;

    public AnonymousResultExportPdfUNE2012() {
        basicServiceForm = new BasicServiceForm();
    }

    public AnonymousResultExportPdfUNE2012(final BasicServiceForm basicServiceForm) {
        this.basicServiceForm = basicServiceForm;
        setBasicService(true);
    }

    @Override
    public int createIntroductionChapter(final MessageResources messageResources, final IndexEvents index, final Document document, int countSections, int numChapter, final Font titleFont) throws Exception {
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("ob.resAnon.intav.report.chapter1.title"), index, countSections++, numChapter, titleFont);
        if (!isBasicService()) {
            final SpecialChunk externalLink = new SpecialChunk("Observatorio de Accesibilidad Web", ConstantsFont.ANCHOR_FONT);
            externalLink.setExternalLink(true);
            externalLink.setAnchor("http://administracionelectronica.gob.es/PAe/accesibilidad");
            final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
            specialChunkMap.put(1, externalLink);


            final String intro = "Mediante el [anchor1] se pretende ayudar a las Administraciones Públicas en el cumplimiento de los requisitos de accesibilidad vigentes. ";
            chapter.add(PDFUtils.createParagraphAnchor(intro, specialChunkMap, ConstantsFont.PARAGRAPH));

            PDFUtils.addParagraph("La realización de las iteraciones periódicas del Observatorio de Accesibilidad Web permite conocer el grado de cumplimiento de los principios de Accesibilidad Web, cómo éste va evolucionando a lo largo del tiempo, y los principales problemas que hay que resolver. De esta forma se consigue extraer las conclusiones y planes de acción adecuados para apoyar a las organizaciones a alcanzar el siguiente " +
                    "objetivo: Conseguir afianzar un nivel óptimo de cumplimiento de forma sostenible en el tiempo.", ConstantsFont.PARAGRAPH, chapter);

            final SpecialChunk anchor = new SpecialChunk("ANEXO I", "anchor_annex", false, ConstantsFont.ANCHOR_FONT);
            specialChunkMap.put(1, anchor);
            chapter.add(PDFUtils.createParagraphAnchor("Las diferentes iteraciones del Estudio de Observatorio se realizan según una metodología propia acordada (disponible en el [anchor1]) y que supone una abstracción de los principios de accesibilidad en función de un conjunto de evaluaciones significativas. De este modo se consigue una estimación del estado de accesibilidad de los portales.", specialChunkMap, ConstantsFont.PARAGRAPH));
            //createChapter1(messageResources, chapter);

            final Section section1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter11.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            createSection11(messageResources, section1);
        } else {
            final ArrayList<String> boldWords = new ArrayList<>();
            boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("El {0} realiza un análisis estimativo del grado de cumplimiento en materia de accesibilidad de los portales de las Administraciones Públicas realizando iteraciones oficiales en los ámbitos:  Administración General del Estado, Comunidades Autónomas y Entidades Locales. Todo ello con el objetivo de ofrecer un informe de situación que ayude a mejorar la accesibilidad de estos portales y centrar los esfuerzos futuros.", boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

            final String masInfo = "Más información en ";
            final String link = "http://administracionelectronica.gob.es/PAe/accesibilidad";
            Paragraph p = new Paragraph(masInfo, ConstantsFont.PARAGRAPH);
            p.setSpacingBefore(LINE_SPACE);
            p.add(PDFUtils.createPhraseLink(link, link, ConstantsFont.LINK_FONT));
            chapter.add(p);

            final String mensajeEmitidoAPeticion = "Este informe concreto se emite a {0}, herramienta puesta a disposición por el Observatorio de Accesibilidad Web, para su uso por parte de cualquier portal de las Administraciones Públicas.";
            boldWords.clear();
            boldWords.add("solicitud del interesado desde el Servicio de Diagnóstico en línea");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(mensajeEmitidoAPeticion, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

            boldWords.clear();
            boldWords.add("ofrece una estimación de la situación general de la accesibilidad en ese portal");

            final String annexAnchor = "ANEXO I";
            final String parrafoEstimacionSituacionPreLink = "El informe {0} basado en la metodología acordada (";
            final String parrafoEstimacionSituacionPostLink = ") pero no se trata de una \"Auditoría de Accesibilidad\".";
            p = PDFUtils.createParagraphWithDiferentFormatWord(parrafoEstimacionSituacionPreLink, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
            Chunk chunk = new Chunk(annexAnchor, ConstantsFont.ANCHOR_FONT);
            chunk.setLocalGoto("anchor_annex");
            p.add(chunk);
            p.add(PDFUtils.createPhrase(parrafoEstimacionSituacionPostLink, ConstantsFont.PARAGRAPH));
            chapter.add(p);

            boldWords.clear();
            boldWords.add("Guía de validación de accesibilidad web");
            final String revisionManual = "Para revisar la accesibilidad de un portal es necesario complementarlo con un análisis exhaustivo utilizando tanto herramientas manuales como automáticas. Para ayudar en este proceso puede consultarse la Guía Práctica del Observatorio de Accesibilidad Web \"{0}\"  disponible en ";
            p = PDFUtils.createParagraphWithDiferentFormatWord(revisionManual, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
            final String linkDocumentacion = "http://administracionelectronica.gob.es/PAe/accesibilidad/documentacion";
            p.add(PDFUtils.createPhraseLink(linkDocumentacion, linkDocumentacion, ConstantsFont.LINK_FONT));
            chapter.add(p);

        }
        document.add(chapter);

        return countSections;
    }

    protected void createChapter1(final MessageResources messageResources, Chapter chapter) {
        final ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.1.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.1.p2.bold"));
        Paragraph p = PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.1.p2"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        Phrase ph = new Phrase(messageResources.getMessage("ob.resAnon.intav.report.1.p2.m1"), ConstantsFont.paragraphUnderlinedFont);
        p.add(ph);
        chapter.add(p);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.1.p3.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.1.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        if (isBasicService()) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.1.p4"), ConstantsFont.PARAGRAPH, chapter);
        }
    }

    protected void createSection11(final MessageResources messageResources, Section section) {
        final ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.11.p4.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.11.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.11.p5"), ConstantsFont.PARAGRAPH, section);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.11.p1.bold"));
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.11.p1"), ConstantsFont.PARAGRAPH, section);
    }

    @Override
    public int createObjetiveChapter(final MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, final java.util.List<ObservatoryEvaluationForm> evaList, long observatoryType) throws DocumentException {
        if (isBasicService() && basicServiceForm.isContentAnalysis()) {
            // Añadir el código fuente analizado
            return createContentChapter(messageResources, document, basicServiceForm.getContent(), index, numChapter, countSections);
        } else {
            return createMuestraPaginasChapter(messageResources, index, document, countSections, numChapter, titleFont, evaList);
        }
    }

    @Override
    public int createContentChapter(final MessageResources messageResources, final Document d, final String contents, final IndexEvents index, int numChapter, int countSections) throws DocumentException {
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("basic.service.content.title"), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        PDFUtils.addParagraph(messageResources.getMessage("basic.service.content.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, true);
        PDFUtils.addParagraphCode(HTMLEntities.unhtmlAngleBrackets(contents), "", chapter);

        PDFUtils.addParagraph("El análisis se ha ejecutado con la siguiente configuración:", ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_LEFT, true, false);
        final com.lowagie.text.List listaConfiguracionRastreo = new com.lowagie.text.List();
        listaConfiguracionRastreo.setIndentationLeft(LINE_SPACE);
        PDFUtils.addListItem("Tipo: Código fuente", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem("Normativa: " + basicServiceForm.reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);

        chapter.add(listaConfiguracionRastreo);

        d.add(chapter);
        return countSections;
    }

    private int createMuestraPaginasChapter(final MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, final java.util.List<ObservatoryEvaluationForm> evaList) throws DocumentException {
        final Chapter chapter = PDFUtils.createChapterWithTitle("Muestra de páginas", index, countSections++, numChapter, titleFont, true);
        PDFUtils.addParagraph("A continuación, se incluye la muestra de páginas incluidas en este análisis:", ConstantsFont.PARAGRAPH, chapter);

        //createChapter2(messageResources, index, countSections++, chapter, evaList, observatoryType);
        chapter.add(addURLTable(messageResources, evaList));
        if (isBasicService()) {
            PDFUtils.addParagraph("El análisis se ha ejecutado con la siguiente configuración:", ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_LEFT, true, false);
            final List listaConfiguracionRastreo = new List();
            listaConfiguracionRastreo.setIndentationLeft(LINE_SPACE);
            PDFUtils.addListItem("Tipo: Selección aleatoria", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
            PDFUtils.addListItem("Origen: " + basicServiceForm.getDomain(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
            PDFUtils.addListItem("Profundidad: " + basicServiceForm.getProfundidad(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
            PDFUtils.addListItem("Amplitud: " + basicServiceForm.getAmplitud(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
            PDFUtils.addListItem("Restringido a directorio: " + (basicServiceForm.isInDirectory() ? "Sí" : "No"), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
            PDFUtils.addListItem("Normativa: " + basicServiceForm.reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);

            chapter.add(listaConfiguracionRastreo);
        }
        document.add(chapter);

        return countSections;
    }

    protected void createChapter2(MessageResources messageResources, IndexEvents index, int countSections, Chapter chapter, java.util.List<ObservatoryEvaluationForm> evaList, long observatoryType) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.PARAGRAPH, chapter);
        } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.PARAGRAPH, chapter);
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.PARAGRAPH, chapter);
        }

        Section section = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);
        if (evaList != null) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.PARAGRAPH, section);
            section.add(addURLTable(messageResources, evaList));
            section.newPage();
        }
    }

    @Override
    public int createMethodologyChapter(final MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception {
        final Chapter chapter = PDFUtils.createChapterWithTitle("ANEXO I: " + messageResources.getMessage("ob.resAnon.intav.report.chapter3.title"), index, countSections++, numChapter, titleFont, true, "anchor_annex");
        chapter.setNumberDepth(0);

        //final String anexoMetodologiaParrafo1 = "La revisión de accesibilidad de los estudios del Observatorio de Accesibilidad Web se realiza mediante una metodología automática desarrollada expresamente para este observatorio. Esta metodología tiene en cuenta únicamente 20 verificaciones de accesibilidad. Cada verificación está compuesta por varias comprobaciones realizadas automáticamente. Mediante métricas especializadas se pudieron incluir algunas comprobaciones de revisión habitualmente manual.";
        final String anexoMetodologiaParrafo1 ="La revisión de accesibilidad de los estudios del Observatorio de Accesibilidad Web se realiza mediante una metodología automática desarrollada expresamente para este observatorio. Esta metodología tiene en cuenta únicamente 20 verificaciones de accesibilidad. Cada verificación está compuesta por varias comprobaciones realizadas automáticamente. Se ha hecho un esfuerzo importante en que las verificaciones realizadas sobre cada página no solo consistan en aquellas puramente automáticas, sino que a través de distintos algoritmos y métricas especializadas se han automatizado mediante estimaciones un buen número de comprobaciones cuya revisión es tradicionalmente manual.";
        PDFUtils.addParagraph(anexoMetodologiaParrafo1, ConstantsFont.PARAGRAPH, chapter);
        final String anexoMetodologiaParrafo2 = "La metodología usando el estándar UNE 139803:2012 (WCAG 2.0) se aprobó por el grupo de Trabajo de Sitios Web de la Administración General del Estado y por el Grupo \"Observatorio, Indicadores y Medidas\" del Comité Sectorial de Administración Electrónica (gobiernos regionales y locales).";
        PDFUtils.addParagraph(anexoMetodologiaParrafo2, ConstantsFont.PARAGRAPH, chapter);
        final String anexoMetodologiaParrafo3 = "A continuación se incluye un extracto pero puede consultarla completa en";
        final String anexoMetodologiaParrafo3Link = "http://administracionelectronica.gob.es/PAe/accesibilidad/metodologiaUNE2012";
        Paragraph p = new Paragraph(anexoMetodologiaParrafo3, ConstantsFont.PARAGRAPH);
        p.setSpacingBefore(LINE_SPACE);
        p.add(PDFUtils.createPhraseLink(anexoMetodologiaParrafo3Link, anexoMetodologiaParrafo3Link, ConstantsFont.LINK_FONT));
        chapter.add(p);
        //final String anexoMetodologiaParrafo4 = "La metodología del Observatorio de Accesibilidad se basa en la experiencia de los expertos en accesibilidad y en la realización de distintos observatorios previos.";
        //PDFUtils.addParagraph(anexoMetodologiaParrafo4, ConstantsFont.PARAGRAPH, chapter);
        //final String anexoMetodologiaParrafo5 = "Los análisis de las páginas se efectúan de forma automática por lo que se ha hecho un esfuerzo importante en que las verificaciones realizadas sobre cada página no solo consistan en aquellas puramente automáticas, sino que a través de distintos algoritmos y métricas se han automatizado mediante estimaciones un buen número de comprobaciones cuya revisión es tradicionalmente manual.";
        //PDFUtils.addParagraph(anexoMetodologiaParrafo5, ConstantsFont.PARAGRAPH, chapter);
        //chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.3.p1"), Collections.singletonList(messageResources.getMessage("ob.resAnon.intav.report.3.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        //PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.3.p2"), ConstantsFont.PARAGRAPH, chapter);
        //PDFUtils.addParagraph(messageResources.getMessage("une2012.resAnon.intav.report.3.p3"), ConstantsFont.PARAGRAPH, chapter);

        if (!isBasicService) {
            Section section1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
                createSection31(messageResources, section1, observatoryType, "AGE");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                createSection31(messageResources, section1, observatoryType, "CCAA");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_EELL) {
                createSection31(messageResources, section1, observatoryType, "EELL");
            }
        }

        Section section2 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter32.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection32(messageResources, section2, primaryReportPageList, observatoryType, isBasicService);

        Section section3 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter33.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, messageResources.getMessage("anchor.met.table"));
        createSection33(messageResources, section3, observatoryType);
        Section section31 = PDFUtils.createSection(messageResources.getMessage("une2012.resAnon.intav.report.chapter331.title"), null, ConstantsFont.chapterTitleMPFont3L, section3, countSections++, 2);
        createSection331(messageResources, section31);
        Section section4 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter34.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection34(messageResources, section4);
        Section section41 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter341.title"), null, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection341(messageResources, section41);
        Section section42 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter342.title"), null, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection342(messageResources, section42);
        // Solo sale en el agregado
        if (primaryReportPageList == null) {
            Section section43 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapter343.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
            createSection343(messageResources, section43);
        }

        document.add(chapter);
        return countSections;
    }

    protected void createSection32(final MessageResources messageResources, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) {
        final ArrayList<String> boldWords = new ArrayList<>();
        if (!isBasicService) {
            boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p1.bold"));
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.PARAGRAPH, section);
        } else {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.PARAGRAPH, section);
        }

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.PARAGRAPH, section);

        com.lowagie.text.List list = new com.lowagie.text.List();
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p5.bold"));
        list.add(PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.32.p6.bold"));
        list.add(PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.PARAGRAPH, section);

        PropertiesManager pmgr = new PropertiesManager();
        if (!isBasicService) {
            section.newPage();
        }

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.grafico.rastreo"), messageResources.getMessage("ob.resAnon.intav.report.32.img.alt"), 60);

        if (!isBasicService && observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p8"), ConstantsFont.PARAGRAPH, section);
            com.lowagie.text.List listp8 = new com.lowagie.text.List();
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l1"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l2"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l3"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l4"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l5"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l6"), listp8, ConstantsFont.PARAGRAPH);
            PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.32.p8.l7"), listp8, ConstantsFont.PARAGRAPH);
            listp8.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            section.add(listp8);
        }

        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.PARAGRAPH, section);
            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.PARAGRAPH, section);
        }

//        section.newPage();
//        if (primaryReportPageList != null) {
//            PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.PARAGRAPH, section);
//            section.add(addURLTable(messageResources, primaryReportPageList));
//            section.newPage();
//        }
    }

    protected Section createSection33(final MessageResources messageResources, Section section, long observatoryType) throws BadElementException, IOException {
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.PARAGRAPH, section);

        com.lowagie.text.List list = new com.lowagie.text.List();

        final ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);

        com.lowagie.text.List list2 = new com.lowagie.text.List();
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p4"));
        ListItem itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list2.add(itemL2);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p6"));
        itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p8"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);

        list2 = new com.lowagie.text.List();
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p10"));
        itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("une2012.resAnon.intav.report.33.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list2.add(itemL2);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p12"));
        itemL2 = PDFUtils.addMixFormatListItem(messageResources.getMessage("une2012.resAnon.intav.report.33.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.PARAGRAPH, section);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p15"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p17"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p18"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p19"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p21"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p23"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p25"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.33.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p27.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.33.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p28"), ConstantsFont.PARAGRAPH, section);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold1"));
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold2"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("une2012.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.33.p30.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("une2012.resAnon.intav.report.33.p30"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
        section.add(createMethodologyTable1(messageResources));

        // section.newPage();

        PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
        section.add(createMethodologyTable2(messageResources));

        return section;
    }

    protected PdfPTable createMethodologyTable1(final MessageResources messageResources) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);

        try {
            createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.111", "une2012.resAnon.intav.report.33.table.111.name", "une2012.resAnon.intav.report.33.table.111.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.111.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.111.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.111.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.112", "une2012.resAnon.intav.report.33.table.112.name", "une2012.resAnon.intav.report.33.table.112.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.112.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.112.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.112.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.113", "une2012.resAnon.intav.report.33.table.113.name", "une2012.resAnon.intav.report.33.table.113.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.113.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.113.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.113.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.114", "une2012.resAnon.intav.report.33.table.114.name", "une2012.resAnon.intav.report.33.table.114.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.114.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.114.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.114.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.115", "une2012.resAnon.intav.report.33.table.115.name", "une2012.resAnon.intav.report.33.table.115.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.115.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.115.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.115.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.116", "une2012.resAnon.intav.report.33.table.116.name", "une2012.resAnon.intav.report.33.table.116.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.116.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.116.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.116.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.117", "une2012.resAnon.intav.report.33.table.117.name", "une2012.resAnon.intav.report.33.table.117.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.117.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.117.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.117.modality"));

            createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.121", "une2012.resAnon.intav.report.33.table.121.name", "une2012.resAnon.intav.report.33.table.121.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.121.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.121.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.121.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.122", "une2012.resAnon.intav.report.33.table.122.name", "une2012.resAnon.intav.report.33.table.122.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.122.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.122.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.122.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.123", "une2012.resAnon.intav.report.33.table.123.name", "une2012.resAnon.intav.report.33.table.123.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.123.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.123.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.123.modality"));
        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    protected PdfPTable createMethodologyTable2(final MessageResources messageResources) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);

        try {
            createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.211", "une2012.resAnon.intav.report.33.table.211.name", "une2012.resAnon.intav.report.33.table.211.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.211.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.211.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.211.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.212", "une2012.resAnon.intav.report.33.table.212.name", "une2012.resAnon.intav.report.33.table.212.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.212.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.212.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.212.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.213", "une2012.resAnon.intav.report.33.table.213.name", "une2012.resAnon.intav.report.33.table.213.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.213.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.213.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.213.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.214", "une2012.resAnon.intav.report.33.table.214.name", "une2012.resAnon.intav.report.33.table.214.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.214.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.214.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.214.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.215", "une2012.resAnon.intav.report.33.table.215.name", "une2012.resAnon.intav.report.33.table.215.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.215.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.215.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.215.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.216", "une2012.resAnon.intav.report.33.table.216.name", "une2012.resAnon.intav.report.33.table.216.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.216.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.216.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.216.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.217", "une2012.resAnon.intav.report.33.table.217.name", "une2012.resAnon.intav.report.33.table.217.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.217.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.217.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.217.modality"));

            createMethodologyHeaderTable(messageResources, table, messageResources.getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.221", "une2012.resAnon.intav.report.33.table.221.name", "une2012.resAnon.intav.report.33.table.221.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.221.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.221.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.221.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.222", "une2012.resAnon.intav.report.33.table.222.name", "une2012.resAnon.intav.report.33.table.222.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.222.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.222.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.222.modality"));
            createMethodologyTableRow(messageResources, table, "une2012.resAnon.intav.report.33.table.id.223", "une2012.resAnon.intav.report.33.table.223.name", "une2012.resAnon.intav.report.33.table.223.question",
                    createTextList(messageResources, "une2012.resAnon.intav.report.33.table.223.answer"), createTextList(messageResources, "une2012.resAnon.intav.report.33.table.223.value"), createImageList(messageResources, "une2012.resAnon.intav.report.33.table.223.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    private void createSection331(final MessageResources messageResources, final Section section) throws BadElementException, IOException {
        PDFUtils.addParagraph(messageResources.getMessage("une2012.resAnon.intav.report.331.p1"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(messageResources.getMessage("une2012.resAnon.intav.report.331.p2"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.createTitleTable(messageResources.getMessage("une2012.resAnon.intav.report.331.table.title"), section, 450);
        section.add(create331Table(messageResources));
    }

    private PdfPTable create331Table(final MessageResources messageResources) {
        final float[] widths = {40f, 40f};
        final PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);
        table.setHeaderRows(1);
        create331HeaderTable(messageResources, table);
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.1", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.111"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.2", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.112"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.3", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.113"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.4", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.114"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.5", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.115"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.6", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.116"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.1.7", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.117"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.2.1", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.121"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.2.2", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.122"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.1.2.3", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.123"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.1", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.211"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.2", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.212"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.3", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.213"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.4", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.214"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.5", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.215"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.6", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.216"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.1.7", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.217"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.2.1", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.221"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.2.2", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.222"));
        create331TableRow(messageResources, table, "minhap.observatory.2_0.subgroup.2.2.3", createTextList(messageResources, "une2012.resAnon.intav.report.331.table.verP.223"));

        return table;
    }

    protected void create331HeaderTable(final MessageResources messageResources, final PdfPTable table) {
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("une2012.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("une2012.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void create331TableRow(final MessageResources messageResources, PdfPTable table, String verification, com.lowagie.text.List verP) {
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 10, -1));
        table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected void createSection342(final MessageResources messageResources, final Section section) {
        PDFUtils.addParagraph(messageResources.getMessage("une2012.resAnon.intav.report.342.p1"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(messageResources.getMessage("une2012.resAnon.intav.report.342.p2"), ConstantsFont.PARAGRAPH, section);

        List list = new List();
        PDFUtils.addListItem(messageResources.getMessage("une2012.resAnon.intav.report.342.p3"), list, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem(messageResources.getMessage("une2012.resAnon.intav.report.342.p4"), list, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem(messageResources.getMessage("une2012.resAnon.intav.report.342.p5"), list, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem(messageResources.getMessage("une2012.resAnon.intav.report.342.p6"), list, ConstantsFont.PARAGRAPH, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        final ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p7.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p8.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        list = new List();
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p9.bold"));
        ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p10.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p11.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p12.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        list = new List();
        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p13.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p14.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p15.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p15"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p16.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.342.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p17"), ConstantsFont.PARAGRAPH, section);

        list = new List();
        PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p18"), list, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p19"), list, ConstantsFont.PARAGRAPH, false, true);
        PDFUtils.addListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p20"), list, ConstantsFont.PARAGRAPH, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p21"), ConstantsFont.PARAGRAPH, section);

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "VNP.png", "VNP = SP/NP", 80);

        list = new List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p22.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p23.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p24.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.342.p25"), ConstantsFont.PARAGRAPH, section);

        list = new List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold1"));
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold2"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold1"));
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold2"));
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold3"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold1"));
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold2"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.342.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    @Override
    public void getMidsComparationByVerificationLevelGraphic(HttpServletRequest request, String level, String title, String filePath, String noDataMess, java.util.List<ObservatoryEvaluationForm> evaList, String value, boolean regenerate) throws Exception {
        ResultadosAnonimosObservatorioUNE2012Utils.getMidsComparationByVerificationLevelGraphic(request, level, title, filePath, noDataMess, evaList, value, regenerate);
    }

    @Override
    protected void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, java.util.List<ObservatoryEvaluationForm> evaList) {
        final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
        final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
        final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
        final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
        scoreForm.setVerifications1(labelsL1);
        scoreForm.setVerifications2(labelsL2);
    }

    @Override
    public String getTitle() {
        return "UNE 139803:2012";
    }

}