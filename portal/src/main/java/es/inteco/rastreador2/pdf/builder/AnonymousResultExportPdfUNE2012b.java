/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
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
package es.inteco.rastreador2.pdf.builder;

import static es.inteco.common.ConstantsFont.LINE_SPACE;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012BUtils;

/**
 * AnonymousResultExportPdfUNE2012b. Clase replicada de {@link AnonymousResultExportPdfUNE2012} para la nueva versión de la metodología basada en la misma norma que la mencionada y conservar ambas
 * para futuras consultas o comparativas.
 */
public class AnonymousResultExportPdfUNE2012b extends AnonymousResultExportPdf {
	/** The message resources. */
	private MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B);

	/**
	 * Instantiates a new anonymous result export pdf UNE 2012b.
	 */
	public AnonymousResultExportPdfUNE2012b() {
		super(new BasicServiceForm());
	}

	/**
	 * Instantiates a new anonymous result export pdf UNE 2012b.
	 *
	 * @param basicServiceForm the basic service form
	 */
	public AnonymousResultExportPdfUNE2012b(final BasicServiceForm basicServiceForm) {
		super(basicServiceForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createIntroductionChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Document,
	 * es.gob.oaw.rastreador2.pdf.utils.PdfTocManager, com.lowagie.text.Font)
	 */
	@Override
	public void createIntroductionChapter(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, final Font titleFont) throws Exception {
		final Chapter chapter = PDFUtils.createChapterWithTitle(this.messageResources.getMessage("ob.resAnon.intav.report.chapter1.title"), pdfTocManager, titleFont);
		if (!isBasicService()) {
			final SpecialChunk externalLink = new SpecialChunk("Observatorio de Accesibilidad Web", ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor("http://administracionelectronica.gob.es/PAe/accesibilidad");
			final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
			specialChunkMap.put(1, externalLink);
			final String intro = "Mediante el [anchor1] se pretende ayudar a las Administraciones Públicas en el cumplimiento de los requisitos de accesibilidad vigentes. ";
			chapter.add(PDFUtils.createParagraphAnchor(intro, specialChunkMap, ConstantsFont.PARAGRAPH));
			PDFUtils.addParagraph(
					"La realización de las iteraciones periódicas del Observatorio de Accesibilidad Web permite conocer el grado de cumplimiento de los principios de Accesibilidad Web, cómo éste va evolucionando a lo largo del tiempo, y los principales problemas que hay que resolver. De esta forma se consigue extraer las conclusiones y planes de acción adecuados para apoyar a las organizaciones a alcanzar el siguiente "
							+ "objetivo: Conseguir afianzar un nivel óptimo de cumplimiento de forma sostenible en el tiempo.",
					ConstantsFont.PARAGRAPH, chapter);
			final SpecialChunk anchor = new SpecialChunk("ANEXO I", "anchor_annex", false, ConstantsFont.ANCHOR_FONT);
			specialChunkMap.put(1, anchor);
			chapter.add(PDFUtils.createParagraphAnchor(
					"Las diferentes iteraciones del Estudio de Observatorio se realizan según una metodología propia acordada (disponible en el [anchor1]) y que supone una abstracción de los principios de accesibilidad en función de un conjunto de evaluaciones significativas. De este modo se consigue una estimación del estado de accesibilidad de los portales.",
					specialChunkMap, ConstantsFont.PARAGRAPH));
			final Section section1 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter11.title"), pdfTocManager.getIndex(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
			createSection11(this.messageResources, section1);
		} else {
			final ArrayList<String> boldWords = new ArrayList<>();
			boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
			chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(
					"El {0} realiza un análisis estimativo del grado de cumplimiento en materia de accesibilidad de los portales de las Administraciones Públicas realizando iteraciones oficiales en los ámbitos:  Administración General del Estado, Comunidades Autónomas y Entidades Locales. Todo ello con el objetivo de ofrecer un informe de situación que ayude a mejorar la accesibilidad de estos portales y centrar los esfuerzos futuros.",
					boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
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
	}

	/**
	 * Creates the chapter 1.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 */
	protected void createChapter1(final MessageResources messageResources, Chapter chapter) {
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.1.p1.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.1.p1"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.1.p2.bold"));
		Paragraph p = PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.1.p2"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true);
		Phrase ph = new Phrase(this.messageResources.getMessage("ob.resAnon.intav.report.1.p2.m1"), ConstantsFont.paragraphUnderlinedFont);
		p.add(ph);
		chapter.add(p);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.1.p3.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.1.p3"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		if (isBasicService()) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.1.p4"), ConstantsFont.PARAGRAPH, chapter);
		}
	}

	/**
	 * Creates the section 11.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 */
	protected void createSection11(final MessageResources messageResources, Section section) {
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.11.p4.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.11.p4"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.11.p5"), ConstantsFont.PARAGRAPH, section);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.11.p1.bold"));
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.11.p1"), ConstantsFont.PARAGRAPH, section);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createObjetiveChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Document,
	 * es.gob.oaw.rastreador2.pdf.utils.PdfTocManager, com.lowagie.text.Font, java.util.List, long)
	 */
	@Override
	public void createObjetiveChapter(final MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont, final java.util.List<ObservatoryEvaluationForm> evaList,
			long observatoryType) throws DocumentException {
		if (isBasicService() && getBasicServiceForm().isContentAnalysis()) {
			// Añadir el código fuente analizado
			createContentChapter(this.messageResources, document, getBasicServiceForm().getContent(), pdfTocManager);
		} else {
			createMuestraPaginasChapter(this.messageResources, document, pdfTocManager, titleFont, evaList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createContentChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Document, java.lang.String,
	 * es.gob.oaw.rastreador2.pdf.utils.PdfTocManager)
	 */
	@Override
	public void createContentChapter(final MessageResources messageResources, final Document d, final String contents, final PdfTocManager pdfTocManager) throws DocumentException {
		final Chapter chapter = PDFUtils.createChapterWithTitle(this.messageResources.getMessage("basic.service.content.title"), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
		PDFUtils.addParagraph(this.messageResources.getMessage("basic.service.content.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, true);
		PDFUtils.addCode(HTMLEntities.unhtmlAngleBrackets(contents), chapter);
		PDFUtils.addParagraph("El análisis se ha ejecutado con la siguiente configuración:", ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_LEFT, true, false);
		final com.lowagie.text.List listaConfiguracionRastreo = new com.lowagie.text.List();
		listaConfiguracionRastreo.setIndentationLeft(LINE_SPACE);
		PDFUtils.addListItem("Tipo: Código fuente", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem("Normativa: " + getBasicServiceForm().reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
		chapter.add(listaConfiguracionRastreo);
		d.add(chapter);
	}

	/**
	 * Creates the muestra paginas chapter.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param titleFont        the title font
	 * @param evaList          the eva list
	 * @throws DocumentException the document exception
	 */
	private void createMuestraPaginasChapter(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, final Font titleFont,
			final java.util.List<ObservatoryEvaluationForm> evaList) throws DocumentException {
		assert evaList != null;
		final Chapter chapter = PDFUtils.createChapterWithTitle("Muestra de páginas", pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(), titleFont, true);
		PDFUtils.addParagraph("A continuación, se incluye la muestra de páginas incluidas en este análisis:", ConstantsFont.PARAGRAPH, chapter);
		chapter.add(addURLTable(this.messageResources, evaList));
		if (isBasicService()) {
			PDFUtils.addParagraph("El análisis se ha ejecutado con la siguiente configuración:", ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_LEFT, true, false);
			final List listaConfiguracionRastreo = new List();
			listaConfiguracionRastreo.setIndentationLeft(LINE_SPACE);
			listaConfiguracionRastreo.add(createOrigen(getBasicServiceForm().getDomain()));
			if (getBasicServiceForm().getAnalysisType() == BasicServiceAnalysisType.URL) {
				PDFUtils.addListItem("Forma de selección de páginas: aleatoria", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
//				PDFUtils.addListItem("Profundidad: " + getBasicServiceForm().getProfundidad(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
//				PDFUtils.addListItem("Amplitud: " + getBasicServiceForm().getAmplitud(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
				if ("0".equals(getBasicServiceForm().getComplexity())) {
					PDFUtils.addListItem("Complejidad: " + "Única", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
					PDFUtils.addListItem(" · Profundidad: " + "-", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
					PDFUtils.addListItem(" · Amplitud: " + "-", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
				} else {
					try {
						ComplejidadForm complex = ComplejidadDAO.getById(DataBaseManager.getConnection(), getBasicServiceForm().getComplexity());
						PDFUtils.addListItem("Complejidad: " + complex.getName(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
						PDFUtils.addListItem(" · Profundidad: " + complex.getProfundidad(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
						PDFUtils.addListItem(" · Amplitud: " + complex.getAmplitud(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
					} catch (Exception e) {
						PDFUtils.addListItem("Complejidad: " + "-", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
						PDFUtils.addListItem(" · Profundidad: " + "-", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
						PDFUtils.addListItem(" · Amplitud: " + "-", listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, false);
					}
				}
				PDFUtils.addListItem("Selección restringida a directorio: " + (getBasicServiceForm().isInDirectory() ? "Sí" : "No"), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
			}
			PDFUtils.addListItem("Normativa: " + getBasicServiceForm().reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
			chapter.add(listaConfiguracionRastreo);
		}
		document.add(chapter);
	}

	/**
	 * Creates the origen.
	 *
	 * @param domain the domain
	 * @return the text element array
	 */
	private TextElementArray createOrigen(final String domain) {
		if (getBasicServiceForm().getAnalysisType() == BasicServiceAnalysisType.LISTA_URLS) {
			return new ListItem("Origen: Lista de páginas", ConstantsFont.PARAGRAPH);
		} else {
			final SpecialChunk externalLink = new SpecialChunk(domain, ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(domain);
			final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
			specialChunkMap.put(1, externalLink);
			return new ListItem(PDFUtils.createParagraphAnchor("Origen: [anchor1]", specialChunkMap, ConstantsFont.PARAGRAPH, false));
		}
	}

	/**
	 * Creates the chapter 2.
	 *
	 * @param messageResources the message resources
	 * @param index            the index
	 * @param countSections    the count sections
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param observatoryType  the observatory type
	 */
	protected void createChapter2(MessageResources messageResources, IndexEvents index, int countSections, Chapter chapter, java.util.List<ObservatoryEvaluationForm> evaList, long observatoryType) {
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.PARAGRAPH, chapter);
		} else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.PARAGRAPH, chapter);
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.PARAGRAPH, chapter);
		}
		Section section = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections,
				1);
		if (evaList != null) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.PARAGRAPH, section);
			section.add(addURLTable(this.messageResources, evaList));
			section.newPage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createMethodologyChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Document,
	 * es.gob.oaw.rastreador2.pdf.utils.PdfTocManager, com.lowagie.text.Font, java.util.List, long, boolean)
	 */
	@Override
	public void createMethodologyChapter(final MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont,
			java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception {
		final Chapter chapter = PDFUtils.createChapterWithTitle("ANEXO I: " + this.messageResources.getMessage("ob.resAnon.intav.report.chapter3.title"), pdfTocManager.getIndex(),
				pdfTocManager.addSection(), pdfTocManager.getNumChapter(), titleFont, true, "anchor_annex");
		chapter.setNumberDepth(0);
		final String anexoMetodologiaParrafo1 = "La revisión de accesibilidad de los estudios del Observatorio de Accesibilidad Web se realiza mediante una metodología automática desarrollada expresamente para este observatorio. Esta metodología tiene en cuenta únicamente 20 verificaciones de accesibilidad. Cada verificación está compuesta por varias comprobaciones realizadas automáticamente. Se ha hecho un esfuerzo importante en que las verificaciones realizadas sobre cada página no solo consistan en aquellas puramente automáticas, sino que a través de distintos algoritmos y métricas especializadas se han automatizado mediante estimaciones un buen número de comprobaciones cuya revisión es tradicionalmente manual.";
		PDFUtils.addParagraph(anexoMetodologiaParrafo1, ConstantsFont.PARAGRAPH, chapter);
		final String anexoMetodologiaParrafo2 = "La metodología usando el estándar UNE 139803:2012 (WCAG 2.0) se aprobó por el grupo de Trabajo de Sitios Web de la Administración General del Estado y por el Grupo \"Observatorio, Indicadores y Medidas\" del Comité Sectorial de Administración Electrónica (gobiernos regionales y locales).";
		PDFUtils.addParagraph(anexoMetodologiaParrafo2, ConstantsFont.PARAGRAPH, chapter);
		final String anexoMetodologiaParrafo3 = "A continuación se incluye un extracto pero puede consultarla completa en";
		final String anexoMetodologiaParrafo3Link = "http://administracionelectronica.gob.es/PAe/accesibilidad/metodologiaUNE2012v2";
		Paragraph p = new Paragraph(anexoMetodologiaParrafo3, ConstantsFont.PARAGRAPH);
		p.setSpacingBefore(LINE_SPACE);
		p.add(PDFUtils.createPhraseLink(anexoMetodologiaParrafo3Link, anexoMetodologiaParrafo3Link, ConstantsFont.LINK_FONT));
		chapter.add(p);
		Section section1 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter31.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
				chapter, pdfTocManager.addSection(), 1);
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.31.intro"), ConstantsFont.PARAGRAPH, section1);
		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_AGE, "AGE");
		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_CCAA, "CCAA");
		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_EELL, "EELL");
		Section section2 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter32.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
				chapter, pdfTocManager.addSection(), 1);
		createSection32(this.messageResources, section2, primaryReportPageList, observatoryType, isBasicService);
		Section section3 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter33.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
				chapter, pdfTocManager.addSection(), 1, this.messageResources.getMessage("anchor.met.table"));
		createSection33(this.messageResources, section3, observatoryType);
		Section section31 = PDFUtils.createSection(this.messageResources.getMessage("une2012.resAnon.intav.report.chapter331.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3,
				pdfTocManager.addSection(), 2);
		createSection331(this.messageResources, section31);
		Section section4 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter34.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
				chapter, pdfTocManager.addSection(), 1);
		createSection34(this.messageResources, section4);
		Section section41 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter341.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				pdfTocManager.addSection(), 2);
		this.createSection341(this.messageResources, section41);
		Section section42 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter342.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				pdfTocManager.addSection(), 2);
		createSection342(this.messageResources, section42);
		// Solo sale en el agregado
		if (primaryReportPageList == null) {
			Section section43 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter343.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L,
					section4, pdfTocManager.addSection(), 2);
			createSection343(this.messageResources, section43);
		}
		document.add(chapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createSection341(org.apache.struts.util.MessageResources, com.lowagie.text.Section)
	 */
	/**
	 * Sobrescritrua del método para eliminar secciones que no están en esta nueva metodología
	 */
	@Override
	protected void createSection341(final MessageResources messageResources, final Section section) {
		final PropertiesManager pmgr = new PropertiesManager();
		Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
		SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p1.bold"), messageResources.getMessage("anchor.PMP"), true, ConstantsFont.paragraphBoldFont);
		anchorMap.put(1, anchor);
		section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p1"), anchorMap, ConstantsFont.PARAGRAPH));
		final String imagesPath = pmgr.getValue(Constants.PDF_PROPERTIES, "path.images");
		PDFUtils.addImageToSection(section, imagesPath + "PMP.png", "PMP = SRV/VP*10", 80);
		com.lowagie.text.List list = new com.lowagie.text.List();
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p2"));
		ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p4"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p6"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		anchorMap = new HashMap<>();
		anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p8.bold"), messageResources.getMessage("anchor.PMPO"), true, ConstantsFont.paragraphBoldFont);
		anchorMap.put(1, anchor);
		section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p8"), anchorMap, ConstantsFont.PARAGRAPH));
		PDFUtils.addImageToSection(section, imagesPath + "PMPO.png", "PMPO = SPMP/NP", 80);
		list = new com.lowagie.text.List();
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p9"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p11"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p13"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		anchorMap = new HashMap<>();
		anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p15.bold"), messageResources.getMessage("anchor.PMV"), true, ConstantsFont.paragraphBoldFont);
		anchorMap.put(1, anchor);
		section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p15"), anchorMap, ConstantsFont.PARAGRAPH));
		PDFUtils.addImageToSection(section, imagesPath + "PMV.png", "PMV = SR/PP*10", 80);
		list = new com.lowagie.text.List();
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p16"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p17"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p18"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p20"));
		item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		boldWords.clear();
		boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p29.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.341.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
	}

	/**
	 * Creates the section 32.
	 *
	 * @param messageResources      the message resources
	 * @param section               the section
	 * @param primaryReportPageList the primary report page list
	 * @param observatoryType       the observatory type
	 * @param isBasicService        the is basic service
	 */
	protected void createSection32(final MessageResources messageResources, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType,
			boolean isBasicService) {
		final ArrayList<String> boldWords = new ArrayList<>();
		if (!isBasicService) {
			boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.32.p1.bold"));
			section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont,
					ConstantsFont.PARAGRAPH, true));
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.PARAGRAPH, section);
		} else {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.PARAGRAPH, section);
		}
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.PARAGRAPH, section);
		com.lowagie.text.List list = new com.lowagie.text.List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.32.p5.bold"));
		list.add(PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.32.p6.bold"));
		list.add(PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.PARAGRAPH, section);
		PropertiesManager pmgr = new PropertiesManager();
		if (!isBasicService) {
			section.newPage();
		}
		PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.grafico.rastreo.33"), this.messageResources.getMessage("ob.resAnon.intav.report.32.img.alt"), 60);
		if (!isBasicService && observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8"), ConstantsFont.PARAGRAPH, section);
			com.lowagie.text.List listp8 = new com.lowagie.text.List();
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l1"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l2"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l3"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l4"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l5"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l6"), listp8, ConstantsFont.PARAGRAPH);
			PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.32.p8.l7"), listp8, ConstantsFont.PARAGRAPH);
			listp8.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
			section.add(listp8);
		}
		if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.PARAGRAPH, section);
			PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.PARAGRAPH, section);
		}
	}

	/**
	 * Creates the section 33.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param observatoryType  the observatory type
	 * @return the section
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	protected Section createSection33(final MessageResources messageResources, Section section, long observatoryType) throws BadElementException, IOException {
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.PARAGRAPH, section);
		com.lowagie.text.List list = new com.lowagie.text.List();
		com.lowagie.text.List list2 = new com.lowagie.text.List();
		ListItem item = null;
		ListItem itemL2 = null;
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p8"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list2 = new com.lowagie.text.List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p10"));
		itemL2 = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("une2012.resAnon.intav.report.33.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p12"));
		itemL2 = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("une2012.resAnon.intav.report.33.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list2.add(itemL2);
		list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		item.add(list2);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.PARAGRAPH, section);
		list = new com.lowagie.text.List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p15"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p17"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p18"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p19"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p21"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p23"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p25"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.33.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p27.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.33.p27"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.33.p28"), ConstantsFont.PARAGRAPH, section);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold1"));
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p29.bold2"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("une2012.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.33.p30.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("une2012.resAnon.intav.report.33.p30"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.createTitleTable(this.messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
		section.add(createMethodologyTable1(this.messageResources));
		// section.newPage();
		PDFUtils.createTitleTable(this.messageResources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
		section.add(createMethodologyTable2(this.messageResources));
		return section;
	}

	/**
	 * Creates the methodology table 1.
	 *
	 * @param messageResources the message resources
	 * @return the pdf P table
	 */
	protected PdfPTable createMethodologyTable1(final MessageResources messageResources) {
		float[] widths = { 10f, 30f, 45f, 25f, 15f, 15f };
		PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		try {
			createMethodologyHeaderTable(this.messageResources, table, this.messageResources.getMessage("ob.resAnon.intav.report.33.table.title1"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.1.id", "une2012_b.resAnon.intav.report.33.table.1.1.name",
					"une2012_b.resAnon.intav.report.33.table.1.1.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.1.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.1.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.1.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.2.id", "une2012_b.resAnon.intav.report.33.table.1.2.name",
					"une2012_b.resAnon.intav.report.33.table.1.2.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.2.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.2.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.2.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.3.id", "une2012_b.resAnon.intav.report.33.table.1.3.name",
					"une2012_b.resAnon.intav.report.33.table.1.3.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.3.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.3.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.3.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.4.id", "une2012_b.resAnon.intav.report.33.table.1.4.name",
					"une2012_b.resAnon.intav.report.33.table.1.4.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.4.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.4.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.4.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.5.id", "une2012_b.resAnon.intav.report.33.table.1.5.name",
					"une2012_b.resAnon.intav.report.33.table.1.5.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.5.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.5.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.5.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.6.id", "une2012_b.resAnon.intav.report.33.table.1.6.name",
					"une2012_b.resAnon.intav.report.33.table.1.6.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.6.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.6.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.6.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.7.id", "une2012_b.resAnon.intav.report.33.table.1.7.name",
					"une2012_b.resAnon.intav.report.33.table.1.7.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.7.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.7.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.7.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.8.id", "une2012_b.resAnon.intav.report.33.table.1.8.name",
					"une2012_b.resAnon.intav.report.33.table.1.8.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.8.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.8.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.8.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.9.id", "une2012_b.resAnon.intav.report.33.table.1.9.name",
					"une2012_b.resAnon.intav.report.33.table.1.9.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.9.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.9.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.9.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.10.id", "une2012_b.resAnon.intav.report.33.table.1.10.name",
					"une2012_b.resAnon.intav.report.33.table.1.10.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.10.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.10.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.10.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.11.id", "une2012_b.resAnon.intav.report.33.table.1.11.name",
					"une2012_b.resAnon.intav.report.33.table.1.11.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.11.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.11.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.11.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.12.id", "une2012_b.resAnon.intav.report.33.table.1.12.name",
					"une2012_b.resAnon.intav.report.33.table.1.12.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.12.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.12.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.12.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.13.id", "une2012_b.resAnon.intav.report.33.table.1.13.name",
					"une2012_b.resAnon.intav.report.33.table.1.13.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.13.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.13.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.13.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.1.14.id", "une2012_b.resAnon.intav.report.33.table.1.14.name",
					"une2012_b.resAnon.intav.report.33.table.1.14.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.14.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.14.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.1.14.modality"));
		} catch (Exception e) {
			Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return table;
	}

	/**
	 * Creates the methodology table 2.
	 *
	 * @param messageResources the message resources
	 * @return the pdf P table
	 */
	protected PdfPTable createMethodologyTable2(final MessageResources messageResources) {
		float[] widths = { 8f, 30f, 40f, 32f, 15f, 15f };
		PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		try {
			createMethodologyHeaderTable(this.messageResources, table, this.messageResources.getMessage("ob.resAnon.intav.report.33.table.title2"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.1.id", "une2012_b.resAnon.intav.report.33.table.2.1.name",
					"une2012_b.resAnon.intav.report.33.table.2.1.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.1.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.1.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.1.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.2.id", "une2012_b.resAnon.intav.report.33.table.2.2.name",
					"une2012_b.resAnon.intav.report.33.table.2.2.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.2.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.2.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.2.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.3.id", "une2012_b.resAnon.intav.report.33.table.2.3.name",
					"une2012_b.resAnon.intav.report.33.table.2.3.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.3.answer"),
					createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.3.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.3.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.4.id", "une2012_b.resAnon.intav.report.33.table.2.4.name",
					"une2012_b.resAnon.intav.report.33.table.2.4.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.4.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.4.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.4.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.5.id", "une2012_b.resAnon.intav.report.33.table.2.5.name",
					"une2012_b.resAnon.intav.report.33.table.2.5.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.5.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.5.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.5.modality"));
			this.createMethodologyTableRow(this.messageResources, table, "une2012_b.resAnon.intav.report.33.table.2.6.id", "une2012_b.resAnon.intav.report.33.table.2.6.name",
					"une2012_b.resAnon.intav.report.33.table.2.6.question", this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.6.answer"),
					this.createTextList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.6.value"),
					createImageList(this.messageResources, "une2012_b.resAnon.intav.report.33.table.2.6.modality"));
		} catch (Exception e) {
			Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return table;
	}

	@Override
	protected void createMethodologyTableRow(final MessageResources messageResources, final PdfPTable table, final String id, final String name, final String question,
			final com.lowagie.text.List answer, final com.lowagie.text.List value, final com.lowagie.text.List modality) {
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage(id), Color.WHITE, ConstantsFont.noteCellFont7, Element.ALIGN_CENTER, 0, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage(name), Color.WHITE, ConstantsFont.noteCellFont7, Element.ALIGN_CENTER, 0, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage(question), Color.WHITE, ConstantsFont.noteCellFont7, Element.ALIGN_LEFT, 1, -1));
		table.addCell(PDFUtils.createListTableCell(answer, Color.WHITE, Element.ALIGN_LEFT, 0));
		table.addCell(PDFUtils.createListTableCell(value, Color.WHITE, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createListTableCell(modality, Color.WHITE, Element.ALIGN_CENTER, 0));
	}

	@Override
	protected com.lowagie.text.List createTextList(final MessageResources messageResources, final String text) {
		return this.createTextList(messageResources, text, Element.ALIGN_LEFT);
	}

	@Override
	protected com.lowagie.text.List createTextList(final MessageResources messageResources, final String text, final int align) {
		final java.util.List<String> list = Arrays.asList(messageResources.getMessage(text).split(";"));
		final com.lowagie.text.List pdfList = new com.lowagie.text.List();
		for (String str : list) {
			PDFUtils.addListItem(str, pdfList, ConstantsFont.noteCellFont7, false, false, align);
		}
		if (align == Element.ALIGN_LEFT) {
			pdfList.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
		}
		return pdfList;
	}

	/**
	 * Creates the section 331.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	private void createSection331(final MessageResources messageResources, final Section section) throws BadElementException, IOException {
		PDFUtils.addParagraph(this.messageResources.getMessage("une2012.resAnon.intav.report.331.p1"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addParagraph(this.messageResources.getMessage("une2012.resAnon.intav.report.331.p2"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.createTitleTable(this.messageResources.getMessage("une2012.resAnon.intav.report.331.table.title"), section, 450);
		section.add(create331Table(this.messageResources));
	}

	/**
	 * Creates the 331 table.
	 *
	 * @param messageResources the message resources
	 * @return the pdf P table
	 */
	private PdfPTable create331Table(final MessageResources messageResources) {
		final float[] widths = { 40f, 40f };
		final PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setWidthPercentage(100);
		table.setHeaderRows(1);
		create331HeaderTable(this.messageResources, table);
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.1", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.1"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.2", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.2"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.3", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.3"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.4", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.4"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.5", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.5"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.6", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.6"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.7", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.7"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.8", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.8"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.9", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.9"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.10", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.10"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.11", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.11"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.12", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.12"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.13", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.13"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.1.14", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.14"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.1", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.1"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.2", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.2"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.3", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.3"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.4", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.4"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.5", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.5"));
		create331TableRow(this.messageResources, table, "minhap.observatory.3_0.subgroup.2.6", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.6"));
		return table;
	}

	/**
	 * Creates the 331 header table.
	 *
	 * @param messageResources the message resources
	 * @param table            the table
	 */
	protected void create331HeaderTable(final MessageResources messageResources, final PdfPTable table) {
		table.addCell(PDFUtils.createTableCell(this.messageResources.getMessage("une2012.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(this.messageResources.getMessage("une2012.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, 0));
	}

	/**
	 * Creates the 331 table row.
	 *
	 * @param messageResources the message resources
	 * @param table            the table
	 * @param verification     the verification
	 * @param verP             the ver P
	 */
	protected void create331TableRow(final MessageResources messageResources, PdfPTable table, String verification, com.lowagie.text.List verP) {
		table.addCell(PDFUtils.createTableCell(this.messageResources.getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont7, Element.ALIGN_LEFT, 10, -1));
		table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
	}

	/**
	 * Creates the section 342.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 */
	protected void createSection342(final MessageResources messageResources, final Section section) {
		PDFUtils.addParagraph(this.messageResources.getMessage("une2012.resAnon.intav.report.342.p1"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addParagraph(this.messageResources.getMessage("une2012.resAnon.intav.report.342.p2"), ConstantsFont.PARAGRAPH, section);
		List list = new List();
		PDFUtils.addListItem(this.messageResources.getMessage("une2012.resAnon.intav.report.342.p3"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(this.messageResources.getMessage("une2012.resAnon.intav.report.342.p4"), list, ConstantsFont.PARAGRAPH, false, true);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p7.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.342.p7"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p8.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.342.p8"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		list = new List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.bold"));
		ListItem item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		// Sublista
		List sublist = new List();
		final ArrayList<String> subboldWords = new ArrayList<>();
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.1.a"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.1.b"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.1.c"));
		ListItem sublistitem = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.1"), subboldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, false);
		sublist.add(sublistitem);
		subboldWords.clear();
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.2.a"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.2.b"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.2.c"));
		sublistitem = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p9.2"), subboldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				false);
		sublist.add(sublistitem);
		sublist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE * 2);
		section.add(sublist);
		list = new List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.bold"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		// Sublista
		sublist = new List();
		subboldWords.clear();
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.1.a"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.1.b"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.1.c"));
		sublistitem = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.1"), subboldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				false);
		sublist.add(sublistitem);
		subboldWords.clear();
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.2.a"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.2.b"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.2.c"));
		sublistitem = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.2"), subboldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				false);
		sublist.add(sublistitem);
		sublist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE * 2);
		section.add(sublist);
		list = new List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p11.bold"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		// Sublista
		sublist = new List();
		subboldWords.clear();
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p11.1.a"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p11.1.b"));
		subboldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p11.1.c"));
		sublistitem = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p10.1"), subboldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				false);
		sublist.add(sublistitem);
		sublist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE * 2);
		section.add(sublist);
//		boldWords.clear();
//		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p12.bold"));
//		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.342.p12"), boldWords, ConstantsFont.paragraphBoldFont,
//				ConstantsFont.PARAGRAPH, true));
//
//		list = new List();
//		boldWords.clear();
//		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p13.bold"));
//		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
//		list.add(item);
//
//		boldWords.clear();
//		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p14.bold"));
//		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
//		list.add(item);
//
//		boldWords.clear();
//		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p15.bold"));
//		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p15"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
//		list.add(item);
//
//		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
//		section.add(list);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p16.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(this.messageResources.getMessage("ob.resAnon.intav.report.342.p16"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.342.p17"), ConstantsFont.PARAGRAPH, section);
		list = new List();
		PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p18"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p19"), list, ConstantsFont.PARAGRAPH, false, true);
		PDFUtils.addListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p20"), list, ConstantsFont.PARAGRAPH, false, true);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.342.p21"), ConstantsFont.PARAGRAPH, section);
		PropertiesManager pmgr = new PropertiesManager();
		PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "VNP.png", "VNP = SP/NP", 80);
		list = new List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p22.bold"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p23.bold"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p24.bold"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.342.p25"), ConstantsFont.PARAGRAPH, section);
		list = new List();
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold1"));
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p26.bold2"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold1"));
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold2"));
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p27.bold3"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		boldWords.clear();
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold1"));
		boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.342.p28.bold2"));
		item = PDFUtils.addMixFormatListItem(this.messageResources.getMessage("ob.resAnon.intav.report.342.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
		list.add(item);
		list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		section.add(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# getMidsComparationByVerificationLevelGraphic(org.apache.struts.util. MessageResources, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean)
	 */
	@Override
	public void getMidsComparationByVerificationLevelGraphic(MessageResources messageResources, String level, String title, String filePath, String noDataMess,
			java.util.List<ObservatoryEvaluationForm> evaList, String value, boolean regenerate) throws Exception {
		ResultadosAnonimosObservatorioUNE2012BUtils.getMidsComparationByVerificationLevelGraphic(this.messageResources, new HashMap<String, Object>(), level, title, filePath, noDataMess, evaList,
				value, regenerate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# generateScoresVerificacion(org.apache.struts.util.MessageResources, es.inteco.rastreador2.intav.form.ScoreForm, java.util.List)
	 */
	@Override
	protected void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, java.util.List<ObservatoryEvaluationForm> evaList) {
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNE2012BUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNE2012BUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNE2012BUtils.infoLevelIVerificationMidsComparison(this.messageResources, resultL1);
		final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNE2012BUtils.infoLevelIIVerificationMidsComparison(this.messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf#getTitle()
	 */
	@Override
	public String getTitle() {
		return "UNE 139803:2012";
	}

	@Override
	public void createIntroductionChapter(MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont, boolean isBasicService) throws Exception {
		this.createIntroductionChapter(messageResources, document, pdfTocManager, titleFont);
	}
}