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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;
import static es.inteco.common.ConstantsFont.LINE_SPACE;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.RankingInfo;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioAccesibilidadUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;

/**
 * AnonymousResultExportPdfUNE2012b. Clase replicada de {@link AnonymousResultExportPdfUNE2012} para la nueva versión de la metodología basada en la misma norma que la mencionada y conservar ambas
 * para futuras consultas o comparativas.
 */
public class AnonymousResultExportPdfAccesibilidad extends AnonymousResultExportPdf {
	/** The message resources. */
	private MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);

	/**
	 * Instantiates a new anonymous result export pdf UNE 2012b.
	 */
	public AnonymousResultExportPdfAccesibilidad() {
		super(new BasicServiceForm());
	}

	/**
	 * Instantiates a new anonymous result export pdf UNE 2012b.
	 *
	 * @param basicServiceForm the basic service form
	 */
	public AnonymousResultExportPdfAccesibilidad(final BasicServiceForm basicServiceForm) {
		super(basicServiceForm);
	}

	/**
	 * Creates the introduction chapter.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param titleFont        the title font
	 * @throws Exception the exception
	 */
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
			// P1
			Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
			SpecialChunk externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p1.anchor1.text"), ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p1.anchor1.url"));
			specialChunkMap.put(1, externalLink);
			final SpecialChunk externalLink2 = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p1.anchor2.text"), ConstantsFont.ANCHOR_FONT);
			externalLink2.setExternalLink(true);
			externalLink2.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p1.anchor2.url"));
			specialChunkMap.put(2, externalLink2);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p1"), specialChunkMap, ConstantsFont.PARAGRAPH));
			// P2
			specialChunkMap = new HashMap<>();
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p2.bold1"), ConstantsFont.paragraphBoldFont);
			specialChunkMap.put(1, externalLink);
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p2.anchor1.text"), ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p2.anchor1.url"));
			specialChunkMap.put(2, externalLink);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p2"), specialChunkMap, ConstantsFont.PARAGRAPH));
			// P3
			specialChunkMap = new HashMap<>();
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p3.bold1"), ConstantsFont.paragraphBoldFont);
			specialChunkMap.put(1, externalLink);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p3"), specialChunkMap, ConstantsFont.PARAGRAPH));
			/// createSection11(this.messageResources, section1);
		} else {
//			final ArrayList<String> boldWords = new ArrayList<>();
//			boldWords.add(this.messageResources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
//			chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(
//					"El {0} realiza un análisis estimativo del grado de cumplimiento en materia de accesibilidad de los portales de las Administraciones Públicas realizando iteraciones oficiales en los ámbitos:  Administración General del Estado, Comunidades Autónomas y Entidades Locales. Todo ello con el objetivo de ofrecer un informe de situación que ayude a mejorar la accesibilidad de estos portales y centrar los esfuerzos futuros.",
//					boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
//			final String masInfo = "Más información en ";
//			final String link = "http://administracionelectronica.gob.es/PAe/accesibilidad";
//			Paragraph p = new Paragraph(masInfo, ConstantsFont.PARAGRAPH);
//			p.setSpacingBefore(LINE_SPACE);
//			p.add(PDFUtils.createPhraseLink(link, link, ConstantsFont.LINK_FONT));
//			chapter.add(p);
//			final String mensajeEmitidoAPeticion = "Este informe concreto se emite a {0}, herramienta puesta a disposición por el Observatorio de Accesibilidad Web, para su uso por parte de cualquier portal de las Administraciones Públicas.";
//			boldWords.clear();
//			boldWords.add("solicitud del interesado desde el Servicio de Diagnóstico en línea");
//			chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(mensajeEmitidoAPeticion, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
//			boldWords.clear();
//			boldWords.add("ofrece una estimación de la situación general de la accesibilidad en ese portal");
//			final String annexAnchor = "ANEXO I";
//			final String parrafoEstimacionSituacionPreLink = "El informe {0} basado en la metodología acordada (";
//			final String parrafoEstimacionSituacionPostLink = ") pero no se trata de una \"Auditoría de Accesibilidad\".";
//			p = PDFUtils.createParagraphWithDiferentFormatWord(parrafoEstimacionSituacionPreLink, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
//			Chunk chunk = new Chunk(annexAnchor, ConstantsFont.ANCHOR_FONT);
//			chunk.setLocalGoto("anchor_annex");
//			p.add(chunk);
//			p.add(PDFUtils.createPhrase(parrafoEstimacionSituacionPostLink, ConstantsFont.PARAGRAPH));
//			chapter.add(p);
//			boldWords.clear();
//			boldWords.add("Guía de validación de accesibilidad web");
//			final String revisionManual = "Para revisar la accesibilidad de un portal es necesario complementarlo con un análisis exhaustivo utilizando tanto herramientas manuales como automáticas. Para ayudar en este proceso puede consultarse la Guía Práctica del Observatorio de Accesibilidad Web \"{0}\"  disponible en ";
//			p = PDFUtils.createParagraphWithDiferentFormatWord(revisionManual, boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
//			final String linkDocumentacion = "http://administracionelectronica.gob.es/PAe/accesibilidad/documentacion";
//			p.add(PDFUtils.createPhraseLink(linkDocumentacion, linkDocumentacion, ConstantsFont.LINK_FONT));
//			chapter.add(p);
			Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
			SpecialChunk externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p1.anchor1.text"), ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p1.anchor1.url"));
			specialChunkMap.put(1, externalLink);
			final SpecialChunk externalLink2 = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p1.anchor2.text"), ConstantsFont.ANCHOR_FONT);
			externalLink2.setExternalLink(true);
			externalLink2.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p1.anchor2.url"));
			specialChunkMap.put(2, externalLink2);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p1"), specialChunkMap, ConstantsFont.PARAGRAPH));
			// P2
			specialChunkMap = new HashMap<>();
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p2.bold1"), ConstantsFont.paragraphBoldFont);
			specialChunkMap.put(1, externalLink);
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p2.anchor1.text"), ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(messageResources.getMessage("pdf.accessibility.intro.p2.anchor1.url"));
			specialChunkMap.put(2, externalLink);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p2"), specialChunkMap, ConstantsFont.PARAGRAPH));
			// P3
			specialChunkMap = new HashMap<>();
			externalLink = new SpecialChunk(messageResources.getMessage("pdf.accessibility.intro.p3.bold1"), ConstantsFont.paragraphBoldFont);
			specialChunkMap.put(1, externalLink);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("pdf.accessibility.intro.p3"), specialChunkMap, ConstantsFont.PARAGRAPH));
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

	/**
	 * Creates the objetive chapter.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param titleFont        the title font
	 * @param evaList          the eva list
	 * @param observatoryType  the observatory type
	 * @throws DocumentException the document exception
	 */
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

	/**
	 * Creates the content chapter.
	 *
	 * @param messageResources the message resources
	 * @param d                the d
	 * @param contents         the contents
	 * @param pdfTocManager    the pdf toc manager
	 * @throws DocumentException the document exception
	 */
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
		PDFUtils.addListItem("Metodología: " + getBasicServiceForm().reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
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
				PDFUtils.addListItem("Profundidad: " + getBasicServiceForm().getProfundidad(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
				PDFUtils.addListItem("Amplitud: " + getBasicServiceForm().getAmplitud(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
				PDFUtils.addListItem("Selección restringida a directorio: " + (getBasicServiceForm().isInDirectory() ? "Sí" : "No"), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
			}
			PDFUtils.addListItem("Metodología: " + getBasicServiceForm().reportToString(), listaConfiguracionRastreo, ConstantsFont.PARAGRAPH, false, true);
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

	/**
	 * Creates the methodology chapter.
	 *
	 * @param messageResources      the message resources
	 * @param document              the document
	 * @param pdfTocManager         the pdf toc manager
	 * @param titleFont             the title font
	 * @param primaryReportPageList the primary report page list
	 * @param observatoryType       the observatory type
	 * @param isBasicService        the is basic service
	 * @throws Exception the exception
	 */
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
//		final String anexoMetodologiaParrafo1 = "La revisión de accesibilidad de los estudios del Observatorio de Accesibilidad Web se realiza mediante una metodología automática desarrollada expresamente para este observatorio. Esta metodología tiene en cuenta únicamente 20 verificaciones de accesibilidad. Cada verificación está compuesta por varias comprobaciones realizadas automáticamente. Se ha hecho un esfuerzo importante en que las verificaciones realizadas sobre cada página no solo consistan en aquellas puramente automáticas, sino que a través de distintos algoritmos y métricas especializadas se han automatizado mediante estimaciones un buen número de comprobaciones cuya revisión es tradicionalmente manual.";
//		PDFUtils.addParagraph(anexoMetodologiaParrafo1, ConstantsFont.PARAGRAPH, chapter);
//		final String anexoMetodologiaParrafo2 = "La metodología usando el estándar UNE 139803:2012 (WCAG 2.0) se aprobó por el grupo de Trabajo de Sitios Web de la Administración General del Estado y por el Grupo \"Observatorio, Indicadores y Medidas\" del Comité Sectorial de Administración Electrónica (gobiernos regionales y locales).";
//		PDFUtils.addParagraph(anexoMetodologiaParrafo2, ConstantsFont.PARAGRAPH, chapter);
		final String anexoMetodologiaParrafo3 = "Se puede consultar la metodología del observatorio en";
		final String anexoMetodologiaParrafo3Link = "http://administracionelectronica.gob.es/PAe/accesibilidad/metodologiaUNE2012v2";
		Paragraph p = new Paragraph(anexoMetodologiaParrafo3, ConstantsFont.PARAGRAPH);
		p.setSpacingBefore(LINE_SPACE);
		p.add(PDFUtils.createPhraseLink(anexoMetodologiaParrafo3Link, anexoMetodologiaParrafo3Link, ConstantsFont.LINK_FONT));
		chapter.add(p);
//		Section section1 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter31.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
//				chapter, pdfTocManager.addSection(), 1);
//		PDFUtils.addParagraph(this.messageResources.getMessage("ob.resAnon.intav.report.31.intro"), ConstantsFont.PARAGRAPH, section1);
//		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_AGE, "AGE");
//		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_CCAA, "CCAA");
//		createSection31(this.messageResources, section1, Constants.OBSERVATORY_TYPE_EELL, "EELL");
//		Section section2 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter32.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
//				chapter, pdfTocManager.addSection(), 1);
//		createSection32(this.messageResources, section2, primaryReportPageList, observatoryType, isBasicService);
//		Section section3 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter33.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
//				chapter, pdfTocManager.addSection(), 1, this.messageResources.getMessage("anchor.met.table"));
//		createSection33(this.messageResources, section3, observatoryType);
//		Section section31 = PDFUtils.createSection(this.messageResources.getMessage("une2012.resAnon.intav.report.chapter331.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3,
//				pdfTocManager.addSection(), 2);
//		createSection331(this.messageResources, section31);
//		Section section4 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter34.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
//				chapter, pdfTocManager.addSection(), 1);
//		createSection34(this.messageResources, section4);
//		Section section41 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter341.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
//				pdfTocManager.addSection(), 2);
//		this.createSection341(this.messageResources, section41);
//		Section section42 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter342.title"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
//				pdfTocManager.addSection(), 2);
//		createSection342(this.messageResources, section42);
//		// Solo sale en el agregado
//		if (primaryReportPageList == null) {
//			Section section43 = PDFUtils.createSection(this.messageResources.getMessage("ob.resAnon.intav.report.chapter343.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L,
//					section4, pdfTocManager.addSection(), 2);
//			createSection343(this.messageResources, section43);
//		}
		document.add(chapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# createSection341(org.apache.struts.util.MessageResources, com.lowagie.text.Section)
	 */
	/**
	 * Sobrescritrua del método para eliminar secciones que no están en esta nueva metodología.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
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

	/**
	 * Creates the methodology table row.
	 *
	 * @param messageResources the message resources
	 * @param table            the table
	 * @param id               the id
	 * @param name             the name
	 * @param question         the question
	 * @param answer           the answer
	 * @param value            the value
	 * @param modality         the modality
	 */
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

	/**
	 * Creates the text list.
	 *
	 * @param messageResources the message resources
	 * @param text             the text
	 * @return the com.lowagie.text. list
	 */
	@Override
	protected com.lowagie.text.List createTextList(final MessageResources messageResources, final String text) {
		return this.createTextList(messageResources, text, Element.ALIGN_LEFT);
	}

	/**
	 * Creates the text list.
	 *
	 * @param messageResources the message resources
	 * @param text             the text
	 * @param align            the align
	 * @return the com.lowagie.text. list
	 */
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
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.1", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.1"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.2", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.2"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.3", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.3"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.4", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.4"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.5", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.5"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.6", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.6"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.7", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.7"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.8", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.8"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.9", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.9"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.10", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.10"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.11", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.11"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.12", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.12"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.13", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.13"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.1.14", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.1.14"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.1", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.1"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.2", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.2"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.3", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.3"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.4", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.4"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.5", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.5"));
		create331TableRow(this.messageResources, table, "minhap.observatory.5_0.subgroup.2.6", createTextList(this.messageResources, "une2012_b.resAnon.intav.report.331.table.verP.2.6"));
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

	/**
	 * Gets the mids comparation by verification level graphic.
	 *
	 * @param messageResources the message resources
	 * @param level            the level
	 * @param title            the title
	 * @param filePath         the file path
	 * @param noDataMess       the no data mess
	 * @param evaList          the eva list
	 * @param value            the value
	 * @param regenerate       the regenerate
	 * @return the mids comparation by verification level graphic
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# getMidsComparationByVerificationLevelGraphic(org.apache.struts.util. MessageResources, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean)
	 */
	@Override
	public void getMidsComparationByVerificationLevelGraphic(MessageResources messageResources, String level, String title, String filePath, String noDataMess,
			java.util.List<ObservatoryEvaluationForm> evaList, String value, boolean regenerate) throws Exception {
		ResultadosAnonimosObservatorioUNEEN2019Utils.getMidsComparationByVerificationLevelGraphic(this.messageResources, new HashMap<String, Object>(), level, title, filePath, noDataMess, evaList,
				value, regenerate);
	}

	/**
	 * Generate scores verificacion.
	 *
	 * @param messageResources the message resources
	 * @param scoreForm        the score form
	 * @param evaList          the eva list
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf# generateScoresVerificacion(org.apache.struts.util.MessageResources, es.inteco.rastreador2.intav.form.ScoreForm, java.util.List)
	 */
	@Override
	protected void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, java.util.List<ObservatoryEvaluationForm> evaList) {
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioAccesibilidadUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioAccesibilidadUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioAccesibilidadUtils.infoLevelIVerificationMidsComparison(this.messageResources, resultL1);
		final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioAccesibilidadUtils.infoLevelIIVerificationMidsComparison(this.messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf#getTitle()
	 */
	@Override
	public String getTitle() {
		return "UNE 139803:2012 Accesibilidad";
	}

	/**
	 * Adds the cover page.
	 *
	 * @param document      the document
	 * @param titleText     the title text
	 * @param subtitleText  the subtitle text
	 * @param subtitleText2 the subtitle text 2
	 * @param noticeText    the notice text
	 * @throws DocumentException the document exception
	 */
	public static void addCoverPage(final Document document, final String titleText, final String subtitleText, final String subtitleText2, final String noticeText) throws DocumentException {
		final Paragraph title = new Paragraph(titleText, ConstantsFont.DOCUMENT_TITLE_MP_FONT);
		title.setSpacingBefore(ConstantsFont.TITLE_LINE_SPACE);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		if (subtitleText != null && !subtitleText.isEmpty()) {
			final Paragraph subtitle = new Paragraph(subtitleText, ConstantsFont.DOCUMENT_SUBTITLE_MP_FONT);
			subtitle.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
			subtitle.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(subtitle);
		}
		if (subtitleText2 != null && !subtitleText2.isEmpty()) {
			final Paragraph subtitle = new Paragraph(subtitleText2, ConstantsFont.DOCUMENT_SUBTITLE_2_MP_FONT);
			subtitle.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
			subtitle.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(subtitle);
		}
		if (noticeText != null && !noticeText.isEmpty()) {
			final PdfPTable notice = new PdfPTable(1);
			notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
			notice.addCell(PDFUtils.createTableCell(noticeText, Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, 80));
			document.add(notice);
		}
	}

	/**
	 * Creates the seed details chapter.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param titleFont        the title font
	 * @param seed             the seed
	 * @throws Exception the exception
	 */
	public void createSeedDetailsChapter(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, final Font titleFont, SemillaForm seed) throws Exception {
		final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.seed.detail.title"), pdfTocManager, titleFont);
		chapter.add(Chunk.NEWLINE);
		chapter.add(new Paragraph(messageResources.getMessage("pdf.accessibility.seed.detail.p1"), ConstantsFont.PARAGRAPH));
		SemillaForm fullSeed = SemillaDAO.getSeedById(DataBaseManager.getConnection(), seed.getId());
		java.util.List<EtiquetaForm> etiquetas = fullSeed.getEtiquetas();
		String etiquetasTematica = "";
		String etiquetasGeografia = "";
		String etiquetasRecurrencia = "";
		if (etiquetas != null && !etiquetas.isEmpty()) {
			for (EtiquetaForm etiqueta : etiquetas) {
				if (etiqueta.getClasificacion() != null && "1".equals(etiqueta.getClasificacion().getId())) {
					etiquetasTematica = etiquetasTematica + etiqueta.getName() + ";";
				} else if (etiqueta.getClasificacion() != null && "2".equals(etiqueta.getClasificacion().getId())) {
					etiquetasGeografia = etiquetasGeografia + etiqueta.getName() + ";";
				} else if (etiqueta.getClasificacion() != null && "3".equals(etiqueta.getClasificacion().getId())) {
					etiquetasRecurrencia = etiquetasRecurrencia + etiqueta.getName() + ";";
				}
			}
		}
		final float[] widths = { 40f, 40f };
		final PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
		// Ámbito
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.ambito"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_JUSTIFIED,
				ConstantsFont.DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell((fullSeed.getAmbito() != null) ? fullSeed.getAmbito().getName() : "", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED,
				ConstantsFont.DEFAULT_PADDING, -1));
		// Complejidad
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.complejidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(
				(fullSeed.getComplejidad() != null) ? fullSeed.getComplejidad().getName() + " (" + fullSeed.getComplejidad().getAmplitud() * fullSeed.getComplejidad().getProfundidad() + " páginas)"
						: "",
				Color.white, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		// Temática
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.tematica"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_JUSTIFIED,
				ConstantsFont.DEFAULT_PADDING, -1));
		if (!StringUtils.isEmpty(etiquetasTematica)) {
			table.addCell(PDFUtils.createListTableCell(this.createTextList(etiquetasTematica, Element.ALIGN_LEFT), Color.white, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING));
		} else {
			table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		}
		// Geografia
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.geografia"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_JUSTIFIED,
				ConstantsFont.DEFAULT_PADDING, -1));
		if (!StringUtils.isEmpty(etiquetasGeografia)) {
			table.addCell(PDFUtils.createListTableCell(this.createTextList(etiquetasGeografia, Element.ALIGN_LEFT), Color.white, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING));
		} else {
			table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		}
		// Geografia
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.recurrencia"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		if (!StringUtils.isEmpty(etiquetasRecurrencia)) {
			table.addCell(PDFUtils.createListTableCell(this.createTextList(etiquetasRecurrencia, Element.ALIGN_LEFT), Color.white, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING));
		} else {
			table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		}
		// Discapacidad
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.seed.detail.tabla.discapacidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("No", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, -1));
		chapter.add(table);
		document.add(chapter);
	}

	/**
	 * Creates the text list.
	 *
	 * @param text  the text
	 * @param align the align
	 * @return the com.lowagie.text. list
	 */
	protected com.lowagie.text.List createTextList(final String text, final int align) {
		final java.util.List<String> list = Arrays.asList(text.split(";"));
		final com.lowagie.text.List pdfList = new com.lowagie.text.List();
		for (String str : list) {
			PDFUtils.addListItem(str, pdfList, ConstantsFont.noteCellFont, false, false, align);
		}
		if (align == Element.ALIGN_LEFT) {
			pdfList.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
		}
		return pdfList;
	}

	/**
	 * Adds the observatory score summary.
	 *
	 * @param pdfBuilder                 the pdf builder
	 * @param messageResources           the message resources
	 * @param document                   the document
	 * @param pdfTocManager              the pdf toc manager
	 * @param currentEvaluationPageList  the current evaluation page list
	 * @param previousEvaluationPageList the previous evaluation page list
	 * @param file                       the file
	 * @param rankingActual              the ranking actual
	 * @param rankingPrevio              the ranking previo
	 * @throws Exception the exception
	 */
	public static void addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, Document document, PdfTocManager pdfTocManager,
			final java.util.List<ObservatoryEvaluationForm> currentEvaluationPageList, java.util.List<ObservatoryEvaluationForm> previousEvaluationPageList, final File file,
			final RankingInfo rankingActual, final RankingInfo rankingPrevio) throws Exception {
		final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.global.summary.title").toUpperCase(), pdfTocManager.getIndex(),
				pdfTocManager.addSection(), pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resumen_resultados");
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p1.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p1"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p2.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p2"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p3.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p3"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p4.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p4"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p5.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p5"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		// Info
		final PdfPTable notice = new PdfPTable(new float[] { 100f });
		notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
		notice.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.intro.info"), Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont, Element.ALIGN_JUSTIFIED,
				ConstantsFont.DEFAULT_PADDING, 50));
		chapter.add(notice);
		final ScoreForm currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
		ScoreForm previousScore = null;
		if (previousEvaluationPageList != null && !previousEvaluationPageList.isEmpty()) {
			// Recuperamos el cartucho asociado al analsis
			Connection c = DataBaseManager.getConnection();
			previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
			DataBaseManager.closeConnection(c);
		}
		chapter.add(Chunk.NEWLINE);
		//// TABLA PUNTUCAION OBSERVATORIO
		final float[] columnWidths;
		if (rankingPrevio != null) {
			columnWidths = new float[] { 0.42f, 0.20f, 0.20f, 0.18f };
		} else {
			columnWidths = new float[] { 0.6f, 0.4f };
		}
		final PdfPTable tablaRankings = new PdfPTable(columnWidths);
		tablaRankings.setSpacingBefore(LINE_SPACE);
		tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
		tablaRankings.setWidthPercentage(90);
		tablaRankings.setHeaderRows(1);
		tablaRankings.addCell(PDFUtils.createEmptyTableCell());
		if (pdfBuilder.isBasicService()) {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		} else {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingActual.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingPrevio.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		// AVG Score
		tablaRankings.addCell(PDFUtils.createTableCell("Puntuación media del sitio web", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(currentScore.getTotalScore(), previousScore.getTotalScore()),
					String.valueOf(currentScore.getTotalScore().subtract(previousScore.getTotalScore()).toPlainString()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING,
					-1));
		}
		tablaRankings.completeRow();
		// Adecuación
		tablaRankings.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getLevel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(createEvolutionLevelCell(messageResources, currentScore.getLevel(), previousScore.getLevel()));
		}
		tablaRankings.completeRow();
		if (rankingActual != null) {
			// Posición en categoría
			tablaRankings.addCell(
					PDFUtils.createTableCell(messageResources.getMessage("observatorio.posicion.global"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getGlobalSeedsNumber()) + ")", Color.WHITE,
					ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (rankingPrevio != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getGlobalSeedsNumber()) + ")", Color.WHITE,
						ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getGlobalRank(), rankingPrevio.getGlobalRank(), true),
						String.valueOf(rankingPrevio.getGlobalRank() - rankingActual.getGlobalRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
			// Posición en categoría
			tablaRankings.addCell(
					PDFUtils.createTableCell("Posición en " + rankingActual.getCategoria().getName(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getCategorySeedsNumber()) + ")", Color.WHITE,
					ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (rankingPrevio != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getCategorySeedsNumber()) + ")",
						Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getCategoryRank(), rankingPrevio.getCategoryRank(), true),
						String.valueOf(rankingPrevio.getCategoryRank() - rankingActual.getCategoryRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
			// Posición en complejidad
			tablaRankings.addCell(
					PDFUtils.createTableCell("Posición en " + rankingActual.getComplejidad().getName(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getComplexityRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getCategorySeedsNumber()) + ")",
					Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (rankingPrevio != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getComplexityRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getCategorySeedsNumber()) + ")",
						Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getComplexityRank(), rankingPrevio.getComplexityRank(), true),
						String.valueOf(rankingPrevio.getComplexityRank() - rankingActual.getComplexityRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
		}
		chapter.add(tablaRankings);
		// Gráficos
		final String noDataMess = messageResources.getMessage("grafica.sin.datos");
		// Puntuación media por verificación
		chapter.add(Chunk.NEXTPAGE);
		Section section = PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.title"), pdfTocManager.getIndex(),
				ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
		section.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.p1"), ConstantsFont.PARAGRAPH));
		section.add(Chunk.NEWLINE);
		section.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.p2"), ConstantsFont.PARAGRAPH));
		section.add(Chunk.NEWLINE);
		Section section1 = PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.title.level1"), pdfTocManager.getIndex(),
				ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.addSection(), 1);
		addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section1, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
		section1.add(createObservatoryVerificationScoreTable(messageResources, currentScore, rankingPrevio != null ? previousScore : null, Constants.OBS_PRIORITY_1, pdfBuilder.isBasicService()));
		document.add(chapter);
	}

	/**
	 * Adds the level allocation results summary.
	 *
	 * @param messageResources           the message resources
	 * @param section                    the section
	 * @param file                       the file
	 * @param currentEvaluationPageList  the current evaluation page list
	 * @param previousEvaluationPageList the previous evaluation page list
	 * @param noDataMess                 the no data mess
	 * @param isBasicService             the is basic service
	 * @throws Exception the exception
	 */
	private static void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section, final File file,
			final java.util.List<ObservatoryEvaluationForm> currentEvaluationPageList, final java.util.List<ObservatoryEvaluationForm> previousEvaluationPageList, final String noDataMess,
			final boolean isBasicService) throws Exception {
		final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
		final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);
		final java.util.List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
		final java.util.List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);
		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
		final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
		ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
		final Image image = PDFUtils.createImage(filePath, null);
		if (image != null) {
			image.scalePercent(60);
			image.setAlignment(Element.ALIGN_CENTER);
			image.setSpacingAfter(ConstantsFont.LINE_SPACE);
			section.add(image);
		}
		final float[] columnsWidth;
		if (!previousEvaluationPageList.isEmpty()) {
			columnsWidth = new float[] { 30f, 25f, 30f, 15f };
		} else {
			columnsWidth = new float[] { 35f, 30f, 35f };
		}
		final PdfPTable table = new PdfPTable(columnsWidth);
		table.setWidthPercentage(90);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		if (!previousEvaluationPageList.isEmpty()) {
			table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		}
		table.completeRow();
		final ListIterator<GraphicData> actualDataIterator = currentGlobalAccessibilityLevel.listIterator();
		final ListIterator<GraphicData> previousDataIterator = previousGlobalAccessibilityLevel.listIterator();
		assert currentGlobalAccessibilityLevel.size() == previousGlobalAccessibilityLevel.size();
		while (actualDataIterator.hasNext()) {
			final GraphicData actualData = actualDataIterator.next();
			final GraphicData previousData = previousDataIterator.hasNext() ? previousDataIterator.next() : actualData;
			table.addCell(PDFUtils.createTableCell(actualData.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			if (!previousEvaluationPageList.isEmpty()) {
				try {
					table.addCell(PDFUtils.createTableCell(new DecimalFormat("+0.00;-0.00").format(actualData.getRawPercentage().subtract(previousData.getRawPercentage())) + "%", Color.white,
							ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
				} catch (NumberFormatException nfe) {
					table.addCell(PDFUtils.createTableCell("Errror", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
				}
			}
			table.completeRow();
		}
		if (!isBasicService) {
			table.setSpacingBefore(ConstantsFont.LINE_SPACE * 2f);
		} else {
			table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
		}
		section.add(table);
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue       the actual value
	 * @param previousValue     the previous value
	 * @param invertedEvolution the inverted evolution
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int actualValue, final int previousValue, boolean invertedEvolution) {
		if (invertedEvolution) {
			return getEvolutionImage(previousValue, actualValue);
		} else {
			return getEvolutionImage(actualValue, previousValue);
		}
	}

	/**
	 * Adds the results by page.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param file             the file
	 * @param evaList          the eva list
	 * @param noDataMess       the no data mess
	 * @throws Exception the exception
	 */
	private static void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final java.util.List<ObservatoryEvaluationForm> evaList,
			final String noDataMess) throws Exception {
		chapter.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.pagina.p1"), ConstantsFont.PARAGRAPH));
		chapter.add(Chunk.NEWLINE);
		chapter.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.pagina.p2"), ConstantsFont.PARAGRAPH));
		chapter.add(Chunk.NEWLINE);
		final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
		ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);
		final Image image = PDFUtils.createImage(filePath, null);
		if (image != null) {
			image.scalePercent(70);
			image.setAlignment(Element.ALIGN_CENTER);
			image.setSpacingAfter(ConstantsFont.LINE_SPACE);
			chapter.add(image);
			chapter.add(Chunk.NEWLINE);
		}
		chapter.add(Chunk.NEXTPAGE);
		final float[] widths = { 33f, 33f, 33f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(90);
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.setHeaderRows(1);
		table.setKeepTogether(true);
		int pageCounter = 1;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			table.addCell(
					PDFUtils.createLinkedTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", pageCounter), evaluationForm.getUrl(), Color.white, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1, RoundingMode.UNNECESSARY)), Color.white,
					ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white,
					ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			pageCounter++;
		}
		table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
		table.setSpacingAfter(0);
		chapter.add(table);
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final BigDecimal actualValue, final BigDecimal previousValue) {
		return getEvolutionImage(actualValue.compareTo(previousValue));
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int actualValue, final int previousValue) {
		return getEvolutionImage(actualValue - previousValue);
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param compareValue the compare value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int compareValue) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (compareValue > 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Aumenta");
		} else if (compareValue < 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Disminuye");
		} else {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene");
		}
	}

	/**
	 * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
	 *
	 * @param messageResources the message resources
	 * @param currentLevel     String nivel de accesibilidad actual.
	 * @param previousLevel    String nivel de accesibilidad de la iteración anterior.
	 * @return una celda PdfPCell con una imagen que indica la evolución y una cadena con la misma información complementando la imagen.
	 */
	private static PdfPCell createEvolutionLevelCell(final MessageResources messageResources, final String currentLevel, final String previousLevel) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (currentLevel.equalsIgnoreCase(previousLevel)) {
			return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene"), "se mantiene", Color.WHITE, ConstantsFont.noteCellFont,
					Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
		} else {
			// Si los valores entre iteraciones han variado
			if (messageResources.getMessage("resultados.anonimos.num.portales.nv").equalsIgnoreCase(previousLevel)
					|| messageResources.getMessage("resultados.anonimos.num.portales.parcial").equalsIgnoreCase(previousLevel)) {
				// Si el valor actual es distinto al anterior y el anterior era
				// "No válido" (o "Parcial") entonces ha mejorado
				return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE, ConstantsFont.noteCellFont,
						Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
			} else if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(previousLevel)) {
				// Si el valor actual es distinto al anterior y el anterior era
				// "Prioridad 1 y 2" entonces ha empeorado
				return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE, ConstantsFont.noteCellFont,
						Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
			} else {
				// Si estamos en este punto el valor anterior era "Prioridad 1"
				// y el actual es distinto
				if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(currentLevel)) {
					return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE,
							ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
				} else {
					return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE,
							ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
				}
			}
		}
	}

	/**
	 * Adds the mids comparation by verification level graphic.
	 *
	 * @param pdfBuilder       the pdf builder
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param file             the file
	 * @param evaList          the eva list
	 * @param noDataMess       the no data mess
	 * @param level            the level
	 * @throws Exception the exception
	 */
	private static void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Section section, final File file,
			final java.util.List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
		final String title;
		final String filePath;
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.title");
			filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test2.jpg";
		} else { // if (level.equals(Constants.OBS_PRIORITY_2)) {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.title");
			filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test3.jpg";
		}
		final PropertiesManager pmgr = new PropertiesManager();
		pdfBuilder.getMidsComparationByVerificationLevelGraphic(messageResources, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"),
				true);
		final Image image = PDFUtils.createImage(filePath, null);
		if (image != null) {
			image.scalePercent(60);
			section.add(image);
		}
	}

	/**
	 * Creates the observatory verification score table.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param previousScore    the previous score
	 * @param level            the level
	 * @param basicService     the basic service
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTable(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level,
			boolean basicService) {
		if (!basicService && previousScore != null) {
			return createObservatoryVerificationScoreTableWithEvolution(messageResources, actualScore, previousScore, level);
		} else {
			return createObservatoryVerificationScoreTableNoEvolution(messageResources, actualScore, level);
		}
	}

	/**
	 * Creates the observatory verification score table no evolution.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param level            the level
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTableNoEvolution(final MessageResources messageResources, final ScoreForm actualScore, final String level) {
		final float[] columnsWidths = new float[] { 0.65f, 0.35f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			for (LabelValueBean actualLabelValueBean : actualScore.getVerifications1()) {
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			for (LabelValueBean actualLabelValueBean : actualScore.getVerifications2()) {
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(0);
		return table;
	}

	/**
	 * Creates the observatory verification score table with evolution.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param previousScore    the previous score
	 * @param level            the level
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTableWithEvolution(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore,
			final String level) {
		final float[] columnsWidths = new float[] { 0.55f, 0.25f, 0.20f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications1().listIterator();
			final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications1().listIterator();
			while (actualScoreIterator.hasNext()) {
				final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
				final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel1(), previousScore.getScoreLevel1(), Color.WHITE));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications2().listIterator();
			final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications2().listIterator();
			while (actualScoreIterator.hasNext()) {
				final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
				final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel2(), previousScore.getScoreLevel2(), Color.WHITE));
		}
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(0);
		return table;
	}

	/**
	 * Creates the evolution difference cell value.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @param color         the color
	 * @return the pdf P cell
	 */
	private static PdfPCell createEvolutionDifferenceCellValue(final String actualValue, final String previousValue, final Color color) {
		try {
			return createEvolutionDifferenceCellValue(new BigDecimal(actualValue), new BigDecimal(previousValue), color);
		} catch (NumberFormatException nfe) {
			return createEvolutionDifferenceCellValue(BigDecimal.ZERO, BigDecimal.ZERO, color);
		}
	}

	/**
	 * Creates the evolution difference cell value.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @param color         the color
	 * @return the pdf P cell
	 */
	private static PdfPCell createEvolutionDifferenceCellValue(final BigDecimal actualValue, final BigDecimal previousValue, final Color color) {
		return PDFUtils.createTableCell(getEvolutionImage(actualValue, previousValue), new DecimalFormat("+0.00;-0.00").format(actualValue.subtract(previousValue)), color, ConstantsFont.noteCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1);
	}

	/**
	 * Generate scores.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the score form
	 */
	@Override
	public ScoreForm generateScores(final MessageResources messageResources, final java.util.List<ObservatoryEvaluationForm> evaList) {
		final ScoreForm scoreForm = new ScoreForm();
		int suitabilityGroups = 0;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
			// Codigo duplicado en IntavUtils
			final String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
			if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
			} else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
			} else
				for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
					suitabilityGroups = levelForm.getSuitabilityGroups().size();
					if (levelForm.getName().equalsIgnoreCase("priority 1")) {
						scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
					} else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
						scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
					}
					for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
						if (suitabilityForm.getName().equalsIgnoreCase("A")) {
							scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
						} else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
							scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
						}
					}
				}
		}
		generateScoresVerificacion(messageResources, scoreForm, evaList);
		if (!evaList.isEmpty()) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
		}
		// El nivel de validación del portal
		scoreForm.setLevel(getValidationLevel(scoreForm, messageResources));
		return scoreForm;
	}

	/**
	 * Creates the introduction chapter.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param titleFont        the title font
	 * @param isBasicService   the is basic service
	 * @throws Exception the exception
	 */
	@Override
	public void createIntroductionChapter(MessageResources messageResources, Document document, PdfTocManager pdfTocManager, Font titleFont, boolean isBasicService) throws Exception {
		this.createIntroductionChapter(messageResources, document, pdfTocManager, titleFont);
	}
}