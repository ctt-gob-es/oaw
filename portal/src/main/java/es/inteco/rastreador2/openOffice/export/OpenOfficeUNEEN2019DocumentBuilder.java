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
package es.inteco.rastreador2.openOffice.export;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfTableCellProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableColumnProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTextProperties;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;

/**
 * Clase encargada de construir el documento OpenOffice con los resultados del observatorio usando la metodología UNE 2012 - VERSIÓN 2017.
 */
public class OpenOfficeUNEEN2019DocumentBuilder extends OpenOfficeDocumentBuilder {
	/** The Constant FO_CLIP. */
	private static final String FO_CLIP = "fo:clip";
	/** The Constant STYLE_GRAPHIC_PROPERTIES. */
	private static final String STYLE_GRAPHIC_PROPERTIES = "style:graphic-properties";
	/** The Constant OBSERVATORY_GRAPHIC_ASPECT_MID_NAME. */
	private static final String OBSERVATORY_GRAPHIC_ASPECT_MID_NAME = "observatory.graphic.aspect.mid.name";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME = "observatory.graphic.verification.compilance.comparation.level.2.name";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME = "observatory.graphic.verification.compilance.comparation.level.1.name";
	/** The Constant OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_2_NAME. */
	private static final String OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_2_NAME = "observatory.graphic.modality.by.verification.level.2.name";
	/** The Constant OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_2_NAME. */
	private static final String OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_2_NAME = "observatory.graphic.compilance.by.verification.level.2.name";
	/** The Constant OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_1_NAME = "observatory.graphic.compilance.by.verification.level.1.name";
	/** The Constant OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME = "observatory.graphic.modality.by.verification.level.1.name";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_2_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_2_NAME = "observatory.graphic.verification.mid.comparation.level.2.name";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_1_NAME = "observatory.graphic.verification.mid.comparation.level.1.name";
	/** The Constant TABLASPUNTUACIONCOMPLEJIDAD_BOOKMARK. */
	private static final String TABLASPUNTUACIONCOMPLEJIDAD_BOOKMARK = "tablaspuntuacioncomplejidad";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITIVIY_STRACHED_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITIVIY_STRACHED_NAME = "observatory.graphic.global.puntuation.allocation.complexitiviy.strached.name";
	/** The Constant TABLASPUNTUACIONSEGMENTO_BOOKMARK. */
	private static final String TABLASPUNTUACIONSEGMENTO_BOOKMARK = "tablaspuntuacionsegmento";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENT_STRACHED_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENT_STRACHED_NAME = "observatory.graphic.global.puntuation.allocation.segment.strached.name";
	/** The Constant TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK. */
	private static final String TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK = "tablascumplimientocomplejidad";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME = "observatory.graphic.global.puntuation.compilance.complexitiviy.mark.name";
	/** The Constant TABLASCUMPLIMIENTOSEGMENTO_BOOKMARK. */
	private static final String TABLASCUMPLIMIENTOSEGMENTO_BOOKMARK = "tablascumplimientosegmento";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_SEGMENTS_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_SEGMENTS_MARK_NAME = "observatory.graphic.global.puntuation.compilance.segments.mark.name";
	/** The Constant TABLASCOMPLEJIDAD_BOOKMARK. */
	private static final String TABLASCOMPLEJIDAD_BOOKMARK = "tablascomplejidad";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME = "observatory.graphic.global.puntuation.allocation.complexity.mark.name";
	/** The Constant TABLASSEGMENTO_BOOKMARK. */
	private static final String TABLASSEGMENTO_BOOKMARK = "tablassegmento";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENTS_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENTS_MARK_NAME = "observatory.graphic.global.puntuation.allocation.segments.mark.name";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2 = "-4c1.t1.c4-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2 = "-4c1.t1.c3-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2 = "-4c1.t1.c2-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1 = "-4c1.t1.b4-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1 = "-4c1.t1.b2-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1 = "-4c1.t1.b3-";
	/** The Constant OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME. */
	private static final String OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME = "observatory.graphic.compilance.level.allocation.name";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2 = "-41.t1.c4-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2 = "-41.t1.c3-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2 = "-41.t1.c2-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1 = "-41.t1.b4-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1 = "-41.t1.b3-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1 = "-41.t1.b2-";
	/** The Constant OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME. */
	private static final String OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME = "observatory.graphic.accessibility.level.allocation.name";
	/** The Constant DOT. */
	private static final String DOT = ".";
	/** The Constant MIME_TYPE_JPG. */
	private static final String MIME_TYPE_JPG = "image/jpg";
	/** The Constant SLASH. */
	private static final String SLASH = "/";
	/** The Constant PICTURES_ODT_PATH. */
	private static final String PICTURES_ODT_PATH = "Pictures/";
	/** The Constant XLINK_HREF. */
	private static final String XLINK_HREF = "xlink:href";
	/** The Constant DRAW_IMAGE. */
	private static final String DRAW_IMAGE = "//draw:image";
	/** The Constant TEXT_CONTINUE_NUMBERING. */
	private static final String TEXT_CONTINUE_NUMBERING = "text:continue-numbering";
	/** The Constant TEXT_LIST. */
	private static final String TEXT_LIST = "//text:list";
	/** The Constant TEXT_BOOKMARK_START_TEXT_NAME_S. */
	private static final String TEXT_BOOKMARK_START_TEXT_NAME_S = "//text:bookmark-start[@text:name='%s']";
	/** The Constant TEXT_TITLE. */
	private static final String TEXT_TITLE = "//text:title";
	/** The Constant HEADER_PUNTUACIÓN_MEDIA_DE_LOS_PORTALES. */
	private static final String HEADER_PUNTUACIÓN_MEDIA_DE_LOS_PORTALES = "Puntuación Media de los Portales";
	/** The Constant HEADER_NO_CONFORME. */
	private static final String HEADER_NO_CONFORME = "No conforme";
	/** The Constant HEADER_PARCIALMENTE_CONFORME. */
	private static final String HEADER_PARCIALMENTE_CONFORME = "Parcialmente conforme";
	/** The Constant HEADER_TOTALMENTE_CONFORME. */
	private static final String HEADER_TOTALMENTE_CONFORME = "Totalmente conforme";
	/** The Constant HEADER_NIVEL_DE_CONFORMIDAD. */
	private static final String HEADER_NIVEL_DE_CONFORMIDAD = "Nivel de conformidad";
	/** The Constant HEADER_NO_VALIDO. */
	private static final String HEADER_NO_VALIDO = "No válido";
	/** The Constant HEADER_A. */
	private static final String HEADER_A = "A";
	/** The Constant HEADER_AA. */
	private static final String HEADER_AA = "AA";
	/** The Constant HEADER_PORCENTAJE_DE_PORTALES. */
	private static final String HEADER_PORCENTAJE_DE_PORTALES = "Porcentaje de portales";
	/** The Constant HEADER_NIVEL_DE_PRIORIDAD. */
	private static final String HEADER_NIVEL_DE_PRIORIDAD = "Nivel de prioridad";
	/** The Constant FECHA_BOOKMARK. */
	private static final String FECHA_BOOKMARK = "[fecha]";
	/** The Constant CATEGORYSECTION_BOOKMARK. */
	private static final String CATEGORYSECTION_BOOKMARK = "categorysection";
	/** The Constant COMPLEJIDADSECTION_BOOKMARK. */
	private static final String COMPLEJIDADSECTION_BOOKMARK = "complexitysection";
	/** The Constant EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The Constant REGEX_SPACES_1_MORE. */
	private static final String REGEX_SPACES_1_MORE = "\\s+";
	/** The Constant TEXT_H_NODE. */
	private static final String TEXT_H_NODE = "text:h";
	/** The Constant TEXT_P_NODE. */
	private static final String TEXT_P_NODE = "text:p";
	/** The Constant TEXT_SPAN_NODE. */
	private static final String TEXT_SPAN_NODE = "text:span";
	/** The Constant NOMBRESEGMENTO_BOOKMARK. */
	private static final String NOMBRESEGMENTO_BOOKMARK = "--nombresegmento--";
	/** The Constant NOMBRECOMPLEJIDAD_BOOKMARK. */
	private static final String NOMBRECOMPLEJIDAD_BOOKMARK = "--nombrecomplejidad--";
	/** The Constant JPG_EXTENSION. */
	private static final String JPG_EXTENSION = ".jpg";
	/** The Constant IMAGE_JPEG. */
	private static final String IMAGE_JPEG = "image/jpeg";
	/** The Constant EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS. */
	private static final String EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS = "export.open.office.graphic.noResults";
	/** The Constant LEVEL_I_VERIFICATIONS. */
	private static final List<String> LEVEL_I_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION);
	/** The Constant LEVEL_II_VERIFICATIONS. */
	private static final List<String> LEVEL_II_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION);

	/**
	 * Instantiates a new open office document builder.
	 *
	 * @param executionId      the execution id
	 * @param observatoryId    the observatory id
	 * @param tipoObservatorio the tipo observatorio
	 */
	public OpenOfficeUNEEN2019DocumentBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio) {
		super(executionId, observatoryId, tipoObservatorio);
		numImg = 8;
		numSection = 5;
	}

	/**
	 * Builds the document.
	 *
	 * @param request           the request
	 * @param graphicPath       the graphic path
	 * @param date              the date
	 * @param evolution         the evolution
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @return the odf text document
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.openOffice.export.OpenOfficeDocumentBuilder# buildDocument(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, boolean, java.util.List,
	 * java.util.List)
	 */
	public OdfTextDocument buildDocument(final HttpServletRequest request, final String graphicPath, final String date, final boolean evolution,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories) throws Exception {
		final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
		ResultadosAnonimosObservatorioUNEEN2019Utils.generateGraphics(messageResources, executionId, Long.parseLong(request.getParameter(Constants.ID)), observatoryId, graphicPath,
				Constants.MINISTERIO_P, true);
		final OdfTextDocument odt = getOdfTemplate();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final OdfFileDom odfStyles = odt.getStylesDom();
		List<ComplejidadForm> complexitivities = ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1);
		replaceText(odt, odfFileContent, FECHA_BOOKMARK, date);
		replaceText(odt, odfStyles, FECHA_BOOKMARK, date, TEXT_SPAN_NODE);
		// Global sections
		replaceGlobalSection(graphicPath, pageExecutionList, categories, messageResources, odt, odfFileContent, complexitivities);
		// Generate categories document and merge with parent
		for (CategoriaForm category : categories) {
			OdfTextDocument odtCategory = getOdfTemplateCategories();
			OdfFileDom odfFileContentCategory = odtCategory.getContentDom();
			// Modificar para que coja la plantilla de segmentos, haga los reemplazos e introduzca en el documento padre
			final List<ObservatoryEvaluationForm> pageExecutionListCat = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(executionId, Long.parseLong(category.getId()),
					pageExecutionList, false);
			if (pageExecutionListCat != null && !pageExecutionListCat.isEmpty()) {
				String graphicSuffix = "_".concat(category.getName().replaceAll(REGEX_SPACES_1_MORE, EMPTY_STRING));
				replaceSectionAllocationDistributionGrouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCompilandeDistributionGrouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionAllocationPuntuactionGrouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionMidAllocationLevel1Grouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionMidAllocationLevel2Grouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionModalityLevel1Grouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionModalityLevel2Grouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCompilanceByVerificationLevel1Grouped(messageResources, odt, odfFileContent, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCompilanceByVerificationLevel2Grouped(messageResources, odt, odfFileContent, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionAspectsGrouped(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				// Replace titles with group name
				replaceText(odtCategory, odfFileContentCategory, NOMBRESEGMENTO_BOOKMARK, category.getName(), TEXT_SPAN_NODE);
				replaceText(odtCategory, odfFileContentCategory, NOMBRESEGMENTO_BOOKMARK, category.getName(), TEXT_P_NODE);
				replaceText(odtCategory, odfFileContentCategory, NOMBRESEGMENTO_BOOKMARK, category.getName(), TEXT_H_NODE);
				// Rename documentStyles names to avoid conflicts
				renameStyles(odtCategory, odfFileContentCategory, graphicSuffix);
				appendsNodeAndChildsAtMarkerPosition(odt, odfFileContent, odtCategory, odfFileContentCategory, CATEGORYSECTION_BOOKMARK);
				mergePictures(odt, odtCategory, graphicPath);
				odtCategory.close();
				mergeStylesToPrimaryDoc(odt, odtCategory, graphicSuffix);
				mergeFontTypesToPrimaryDoc(odt, odtCategory);
				forceContinueNumbering(odt, odfFileContent);
			}
		}
		// Generate complexities
		for (ComplejidadForm complejidad : ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1)) {
			OdfTextDocument odtComplexity = getOdfTemplateComplexities();
			OdfFileDom odfFileContentComplejidad = odtComplexity.getContentDom();
			// Modificar para que coja la plantilla de segmentos, haga los reemplazos e introduzca en el documento padre
			final List<ObservatoryEvaluationForm> executionListComplejidad = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(executionId, Long.parseLong(complejidad.getId()),
					pageExecutionList, true);
			if (executionListComplejidad != null && !executionListComplejidad.isEmpty()) {
				String graphicSuffix = "_".concat(complejidad.getName().replaceAll(REGEX_SPACES_1_MORE, EMPTY_STRING));
				replaceSectionAllocationDistributionGrouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionCompilandeDistributionGrouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionAllocationPuntuactionGrouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionMidAllocationLevel1Grouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionMidAllocationLevel2Grouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionModalityLevel1Grouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionModalityLevel2Grouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionCompilanceByVerificationLevel1Grouped(messageResources, odt, odfFileContent, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionCompilanceByVerificationLevel2Grouped(messageResources, odt, odfFileContent, graphicPath, graphicSuffix, executionListComplejidad);
				replaceSectionAspectsGrouped(messageResources, odtComplexity, odfFileContentComplejidad, graphicPath, graphicSuffix, executionListComplejidad);
				// Replace titles with group name
				replaceText(odtComplexity, odfFileContentComplejidad, NOMBRECOMPLEJIDAD_BOOKMARK, complejidad.getName(), TEXT_SPAN_NODE);
				replaceText(odtComplexity, odfFileContentComplejidad, NOMBRECOMPLEJIDAD_BOOKMARK, complejidad.getName(), TEXT_P_NODE);
				replaceText(odtComplexity, odfFileContentComplejidad, NOMBRECOMPLEJIDAD_BOOKMARK, complejidad.getName(), TEXT_H_NODE);
				// Rename documentStyles names to avoid conflicts
				renameStyles(odtComplexity, odfFileContentComplejidad, graphicSuffix);
				// Add all DOM from create document to base doc
				appendsNodeAndChildsAtMarkerPosition(odt, odfFileContent, odtComplexity, odfFileContentComplejidad, COMPLEJIDADSECTION_BOOKMARK);
				mergePictures(odt, odtComplexity, graphicPath);
				odtComplexity.close();
				mergeStylesToPrimaryDoc(odt, odtComplexity, graphicSuffix);
				mergeFontTypesToPrimaryDoc(odt, odtComplexity);
				forceContinueNumbering(odt, odfFileContent);
			}
		}
		// Evolution
		if (evolution) {
			// General results
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionData(Long.valueOf(observatoryId),
					Long.valueOf(executionId));
			final Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
			for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
				resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
			}
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionSuitabilityChart(observatoryId, executionId, graphicPath + "EvolucionNivelConformidadCombinada.jpg", pageObservatoryMap);
			replaceSectionEvolutionSuitabilityLevel(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			replaceImageGeneric(odt, graphicPath + "EvolucionNivelConformidadCombinada" + JPG_EXTENSION, "EvolucionNivelConformidadCombinada", MIME_TYPE_JPG);
			// Section compliance
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionComplianceChart(observatoryId, executionId, graphicPath + "EvolucionNivelCumplimientoCombinada.jpg", pageObservatoryMap);
			replaceSectionEvolutionComplianceLevel(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			replaceImageGeneric(odt, graphicPath + "EvolucionNivelCumplimientoCombinada" + JPG_EXTENSION, "EvolucionNivelCumplimientoCombinada", MIME_TYPE_JPG);
			replaceSectionEvolutionAverageScore(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			// Mid by verification
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByVerificationChartSplit(messageResources,
					new String[] { graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg" },
					pageObservatoryMap, LEVEL_I_VERIFICATIONS);
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByVerificationChart(messageResources, graphicPath + "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg",
					pageObservatoryMap, LEVEL_II_VERIFICATIONS);
			replaceSectionEvolutionScoreByVerification(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", MIME_TYPE_JPG);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg", MIME_TYPE_JPG);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg", MIME_TYPE_JPG);
			// TODO Compliance by verification
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionComplianceByVerificationChartSplit(messageResources,
					new String[] { graphicPath + "EvolucionCumplimientiVerificacionNAICombinadaSplit1.jpg", graphicPath + "EvolucionCumplimientiVerificacionNAICombinadaSplit2.jpg" },
					pageObservatoryMap, LEVEL_I_VERIFICATIONS);
			replaceImageGeneric(odt, graphicPath + "EvolucionCumplimientiVerificacionNAICombinadaSplit1.jpg", "EvolucionCumplimientiVerificacionNAICombinadaSplit1", MIME_TYPE_JPG);
			replaceImageGeneric(odt, graphicPath + "EvolucionCumplimientiVerificacionNAICombinadaSplit2.jpg", "EvolucionCumplimientiVerificacionNAICombinadaSplit2", MIME_TYPE_JPG);
			// By aspects
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByAspectChart(messageResources, graphicPath + "EvolucionPuntuacionMediaAspectoCombinada.jpg", pageObservatoryMap);
			replaceSectionEvolutionScoreByAspect(messageResources, odt, odfFileContent, graphicPath, resultsByAspect);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaAspectoCombinada.jpg", MIME_TYPE_JPG);
			// replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaSegmentoCombinada.jpg", MIME_TYPE_JPG);
			// replaceSectionEvolutionScoreBySegment(messageResources, odt, odfFileContent, graphicPath, resultsByAspect, categories);
		}
		// Fix crop images (fo:clip attribute) after merge several documents
		NodeList nodeList = odt.getContentDom().getElementsByTagName(STYLE_GRAPHIC_PROPERTIES);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.removeAttribute(FO_CLIP);
		}
		// Update title
		replaceDocumentTitle(odt, odfFileContent, "");
		// Add generated tables styles
		addTableStyles(odt);
		return odt;
	}

	/**
	 * Replace global section.
	 *
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param complexitivities  the complexitivities
	 * @throws Exception the exception
	 */
	private void replaceGlobalSection(final String graphicPath, final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final MessageResources messageResources,
			final OdfTextDocument odt, final OdfFileDom odfFileContent, List<ComplejidadForm> complexitivities) throws Exception {
		replaceSectionGlobalAccesibilityDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionComparisionPuntuactionAllocationSegment(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSectionComparisionPuntuactionAllocationComplexity(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList);
		replaceSectionComparisionPercentajeCompilanceSegment(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSectionComparisionPercentajeCompilanceComplexitivy(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList);
		replaceSectionComparisionPuntuactionBySegment(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSectionComparisionPuntuactionByComplexity(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList);
		replaceSectionPuntuacionByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionPuntuacionByVerificationLevel2(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionModalityByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionModalityByVerificationLevel2(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionCompilanceByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionCompilanceByVerificationLevel2(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionComparisionAspects(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
	}

	/**
	 * Replace document titl in metadata and in text content.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newTitle       the new title
	 * @throws Exception the exception
	 */
	private void replaceDocumentTitle(final OdfTextDocument odt, final OdfFileDom odfFileContent, String newTitle) throws Exception {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(newTitle)) {
			XPath xpath = odt.getXPath();
			NodeList nodeList = (NodeList) xpath.evaluate(TEXT_TITLE, odfFileContent, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				OdfElement node = (OdfElement) nodeList.item(i);
				node.setTextContent(newTitle);
			}
			odt.getOfficeMetadata().setTitle(newTitle);
		}
	}

	/**
	 * Append at marker position.
	 *
	 * @param odt                    the odt
	 * @param odfFileContent         the odf file content
	 * @param odtCategory            the odt category
	 * @param odfFileContentCategory the odf file content category
	 * @param markername             the markername
	 * @throws Exception the exception
	 */
	@SuppressWarnings("all")
	private void appendsNodeAndChildsAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, OdfTextDocument odtCategory, OdfFileDom odfFileContentCategory, String markername)
			throws Exception {
		XPath xpath = odt.getXPath();
		XPath xpath2 = odtCategory.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_BOOKMARK_START_TEXT_NAME_S, markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			NodeList nodeList2 = odtCategory.getOfficeBody().getFirstChild().getChildNodes();
			for (int o = 0; o < nodeList2.getLength(); o++) {
				Node cloneNode = nodeList2.item(o).cloneNode(true);
				node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(cloneNode), node.getParentNode());
			}
		}
	}

	/**
	 * Append node at marker position.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newNode        the new node
	 * @param markername     the markername
	 * @throws Exception the exception
	 */
	private void appendNodeAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, Node newNode, String markername) throws Exception {
		XPath xpath = odt.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_BOOKMARK_START_TEXT_NAME_S, markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newNode), node.getParentNode());
		}
	}

	/**
	 * Force conitnue numbering.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @throws Exception the exception
	 */
	private void forceContinueNumbering(final OdfTextDocument odt, final OdfFileDom odfFileContent) throws Exception {
		XPath xpath = odt.getXPath();
		NodeList nodeList = (NodeList) xpath.evaluate(TEXT_LIST, odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.setAttribute(TEXT_CONTINUE_NUMBERING, Constants.TRUE);
			node.setAttribute("text:style-name", "LFO3");
		}
	}

	/**
	 * Merge pictures.
	 *
	 * @param odt         the odt
	 * @param odtSource   the odt source
	 * @param graphicPath the graphic path
	 * @throws Exception the exception
	 */
	protected void mergePictures(final OdfTextDocument odt, final OdfTextDocument odtSource, String graphicPath) throws Exception {
		final XPath xpath = odtSource.getXPath();
		final OdfFileDom odfFileContent = odtSource.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(DRAW_IMAGE, odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			String attribute = node.getAttribute(XLINK_HREF);
			if (!StringUtils.isEmpty(attribute) && attribute.startsWith(PICTURES_ODT_PATH)) {
				byte[] b = odtSource.getPackage().getBytes(attribute);
				String tmpName = attribute.substring(attribute.lastIndexOf(SLASH) + 1);
				File createTempFile = File.createTempFile(tmpName.substring(0, tmpName.lastIndexOf(DOT)), JPG_EXTENSION);
				try (FileOutputStream fos = new FileOutputStream(createTempFile)) {
					fos.write(b);
					insertFileInsideODTFile(odt, attribute, createTempFile, MIME_TYPE_JPG);
				}
			}
		}
	}

	/**
	 * Inserts a new file into the odt file package.
	 *
	 * @param odt         OdfTextDocument the ODT text document file to insert into
	 * @param odtFileName String the filename (full path) to use when inserting the new file
	 * @param newFile     File the file to insert
	 * @param mimeType    String the mime type of the file
	 */
	private void insertFileInsideODTFile(final OdfTextDocument odt, final String odtFileName, final File newFile, final String mimeType) {
		if (newFile.exists()) {
			try (FileInputStream fin = new FileInputStream(newFile)) {
				odt.getPackage().insert(fin, odtFileName, mimeType);
			} catch (Exception e) {
				Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice: " + e.getMessage(), ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR);
			}
		}
	}

	/**
	 * Gets the embeded id image.
	 *
	 * @param tipoObservatorio the tipo observatorio
	 * @param name             the name
	 * @return the embeded id image
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.openOffice.export.OpenOfficeDocumentBuilder# getEmbededIdImage(java.lang.Long, java.lang.String)
	 */
	@Override
	protected String getEmbededIdImage(final Long tipoObservatorio, final String name) {
		return OpenOfficeUNEEN2019ImageUtils.getEmbededIdImage(tipoObservatorio, name);
	}

	/**
	 * Replace global section for allocation distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionGlobalAccesibilityDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME);
		replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
		Map<String, Integer> result = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalAccessibilityLevel(messageResources, result);
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1, labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1, labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1, labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2, labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2, labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2, labelValueBean.get(2).getNumberP());
	}

	/**
	 * Replace global section for compliance distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionGlobalCompilanceDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME);
		replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
		numImg++;
		Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		final Map<String, Integer> resultCompilance = ResultadosAnonimosObservatorioUNEEN2019Utils.getSityesByCompliance(results);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalCompilanceLevel(messageResources, resultCompilance);
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1, labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1, labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1, labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2, labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2, labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2, labelValueBean.get(2).getNumberP());
		return numImg;
	}

	/**
	 * Generate table percentaje and portals.
	 *
	 * @param header1  the header 1
	 * @param header2  the header 2
	 * @param columna1 the columna 1
	 * @param columna2 the columna 2
	 * @param columna3 the columna 3
	 * @param name     the name
	 * @param results  the results
	 * @return the string builder
	 */
	private StringBuilder generateTablePercentajeAndPortals(String header1, String header2, String columna1, String columna2, String columna3, String name, List<LabelValueBean> results) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_Allocation_").append(name).append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		// Header row
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Row 1
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Row 2
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Row 3
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Replace global section comparison allocation by segment.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComparisionPuntuactionAllocationSegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<CategoriaForm> categories, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENTS_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
			numImg++;
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, categories);
		// Create table
		String header1 = HEADER_NIVEL_DE_PRIORIDAD;
		String header2 = HEADER_PORCENTAJE_DE_PORTALES;
		String columna1 = HEADER_AA;
		String columna2 = HEADER_A;
		String columna3 = HEADER_NO_VALIDO;
		for (CategoriaForm category : categories) {
			String name = category.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de adecuación: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASSEGMENTO_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(category));
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASSEGMENTO_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASSEGMENTO_BOOKMARK);
			// Estilos de las tablas
			addTableStyles(odt);
		}
		return numImg;
	}

	/**
	 * Replace section comparison point allocation complexity.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexities      the complexities
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionPuntuactionAllocationComplexity(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ComplejidadForm> complexities, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexities);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageResultsByComplexityMap(executionId, pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1));
		String header1 = HEADER_NIVEL_DE_PRIORIDAD;
		String header2 = HEADER_PORCENTAJE_DE_PORTALES;
		String columna1 = HEADER_AA;
		String columna2 = HEADER_A;
		String columna3 = HEADER_NO_VALIDO;
		for (ComplejidadForm complejidad : complexities) {
			String name = complejidad.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de adecuación: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASCOMPLEJIDAD_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(complejidad));
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASCOMPLEJIDAD_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASCOMPLEJIDAD_BOOKMARK);
		}
	}

	/**
	 * Replace section comparison point compliance segment.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionPercentajeCompilanceSegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<CategoriaForm> categories, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_SEGMENTS_MARK_NAME) + i; // Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageCompilanceResultsBySegmentMap(executionId, pageExecutionList,
				categories);
		String header1 = HEADER_NIVEL_DE_CONFORMIDAD;
		String header2 = HEADER_PORCENTAJE_DE_PORTALES;
		String columna1 = HEADER_TOTALMENTE_CONFORME;
		String columna2 = HEADER_PARCIALMENTE_CONFORME;
		String columna3 = HEADER_NO_CONFORME;
		for (CategoriaForm category : categories) {
			String name = category.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de cumplimiento: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASCUMPLIMIENTOSEGMENTO_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, res.get(category));
//			StringBuilder sb = new StringBuilder();
//			sb.append("<table:table table:name='Table_Allocation_").append(name).append("' table:style-name='TableGraphic'>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
//			// Header row
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 1
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 2
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 3
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			sb.append("</table:table>");
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASCUMPLIMIENTOSEGMENTO_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASCUMPLIMIENTOSEGMENTO_BOOKMARK);
		}
	}

	/**
	 * Replace section comparision puntuaction compilance complexitivy.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexitivities  the complexitivities
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionPercentajeCompilanceComplexitivy(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<ComplejidadForm> complexitivities, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexitivities);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageCompilanceResultsByComplexitivityMap(executionId, pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1));
		String header1 = HEADER_NIVEL_DE_CONFORMIDAD;
		String header2 = HEADER_PORCENTAJE_DE_PORTALES;
		String columna1 = HEADER_TOTALMENTE_CONFORME;
		String columna2 = HEADER_PARCIALMENTE_CONFORME;
		String columna3 = HEADER_NO_CONFORME;
		for (ComplejidadForm complejidad : complexitivities) {
			String name = complejidad.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de cumplimiento: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, res.get(complejidad));
//			StringBuilder sb = new StringBuilder();
//			sb.append("<table:table table:name='Table_Allocation_").append(name).append("' table:style-name='TableGraphic'>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
//			// Header row
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 1
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 2
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 3
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			sb.append("</table:table>");
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
		}
	}

	/**
	 * Adds the table styles.
	 *
	 * @param odt the odt
	 * @throws Exception the exception
	 */
	private void addTableStyles(final OdfTextDocument odt) throws Exception {
		OdfOfficeAutomaticStyles styles = odt.getContentDom().getOrCreateAutomaticStyles();
		OdfStyle tableStyle = styles.newStyle(OdfStyleFamily.Table);
		tableStyle.setAttribute("style:name", "TableGraphic");
		tableStyle.setProperty(OdfTableProperties.MarginLeft, "0mm");
		tableStyle.setProperty(OdfTableProperties.RelWidth, "100%");
		tableStyle.setProperty(OdfTableProperties.Width, "160mm");
		tableStyle.setProperty(OdfTableProperties.Align, "left");
		OdfStyle tableStyleColumn1 = styles.newStyle(OdfStyleFamily.TableColumn);
		tableStyleColumn1.setAttribute("style:name", "TableGraphicColumn1");
		tableStyleColumn1.setProperty(OdfTableColumnProperties.ColumnWidth, "76.5mm");
		tableStyleColumn1.setProperty(OdfTableColumnProperties.RelColumnWidth, "4367");
		OdfStyle tableStyleColumn2 = styles.newStyle(OdfStyleFamily.TableColumn);
		tableStyleColumn2.setAttribute("style:name", "TableGraphicColumn2");
		tableStyleColumn2.setProperty(OdfTableColumnProperties.ColumnWidth, "83.5mm");
		tableStyleColumn2.setProperty(OdfTableColumnProperties.RelColumnWidth, "4765");
		OdfStyle tableStyleCellA1 = styles.newStyle(OdfStyleFamily.TableCell);
		tableStyleCellA1.setAttribute("style:name", "TableGraphicCellBgGreen");
		tableStyleCellA1.setProperty(OdfTableCellProperties.BackgroundColor, "#7e9a40");
		tableStyleCellA1.setProperty(OdfTableCellProperties.Border, "0.5pt solid #000000");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingBottom, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingLeft, "1mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingRight, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingTop, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.VerticalAlign, "middle");
		OdfStyle tableStyleCellA2 = styles.newStyle(OdfStyleFamily.TableCell);
		tableStyleCellA2.setAttribute("style:name", "TableGraphicCellBgWhite");
		tableStyleCellA2.setProperty(OdfTableCellProperties.BackgroundColor, "#ffffff");
		tableStyleCellA2.setProperty(OdfTableCellProperties.Border, "0.5pt solid #000000");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingBottom, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingLeft, "1mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingRight, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingTop, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.VerticalAlign, "middle");
		OdfStyle graphicTableHeader = styles.newStyle(OdfStyleFamily.Paragraph);
		graphicTableHeader.setAttribute("style:name", "GraphicTableHeader");
		graphicTableHeader.setProperty(OdfTextProperties.Color, "#ffffff");
		graphicTableHeader.setProperty(OdfTextProperties.FontName, "Arial");
		graphicTableHeader.setProperty(OdfTextProperties.FontNameComplex, "Times New Roman");
		graphicTableHeader.setProperty(OdfTextProperties.FontSizeAsian, "11pt");
		graphicTableHeader.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		graphicTableHeader.setProperty(OdfTextProperties.FontWeight, "bold");
		graphicTableHeader.setProperty(OdfTextProperties.FontWeightComplex, "bold");
		OdfStyle graphicTableCenter = styles.newStyle(OdfStyleFamily.Paragraph);
		graphicTableCenter.setAttribute("style:name", "GraphicTableCenter");
		graphicTableCenter.setProperty(OdfTextProperties.Color, "#000000");
		graphicTableCenter.setProperty(OdfTextProperties.FontName, "Arial");
		graphicTableCenter.setProperty(OdfTextProperties.FontNameComplex, "Times New Roman");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeAsian, "11pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeight, "normal");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeightComplex, "normal");
	}

	/**
	 * Append paragraph to marker.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param marker         the marker
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws Exception                    the exception
	 */
	private void appendParagraphToMarker(final OdfTextDocument odt, final OdfFileDom odfFileContent, String marker) throws SAXException, IOException, ParserConfigurationException, Exception {
		Element p = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("<text:p text:style-name=\"P\"/>".getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, p, marker);
	}

	/**
	 * Replace section 43.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionPuntuactionBySegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<CategoriaForm> categories, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_SEGMENT_STRACHED_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, categories);
		String header1 = HEADER_NIVEL_DE_PRIORIDAD;
		String header2 = HEADER_PUNTUACIÓN_MEDIA_DE_LOS_PORTALES;
		String columna1 = HEADER_AA;
		String columna2 = HEADER_A;
		String columna3 = HEADER_NO_VALIDO;
		for (CategoriaForm category : categories) {
			String name = category.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Puntuación Media de los Portales: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASPUNTUACIONSEGMENTO_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(category));
//			StringBuilder sb = new StringBuilder();
//			sb.append("<table:table table:name='Table_Allocation_").append(name).append("' table:style-name='TableGraphic'>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
//			// Header row
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 1
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 2
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 3
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			sb.append("</table:table>");
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASPUNTUACIONSEGMENTO_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASPUNTUACIONSEGMENTO_BOOKMARK);
		}
	}

	/**
	 * Replace section 43.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexitivities  the complexitivities
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionPuntuactionByComplexity(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ComplejidadForm> complexitivities, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexitivities);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITIVIY_STRACHED_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateMidPuntuationResultsByComplexitivityMap(executionId, pageExecutionList,
				complexitivities);
		String header1 = HEADER_NIVEL_DE_PRIORIDAD;
		String header2 = HEADER_PUNTUACIÓN_MEDIA_DE_LOS_PORTALES;
		String columna1 = HEADER_AA;
		String columna2 = HEADER_A;
		String columna3 = HEADER_NO_VALIDO;
		for (ComplejidadForm complexitivity : complexitivities) {
			String name = complexitivity.getName();
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Puntuación Media de los Portales: " + name + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASPUNTUACIONCOMPLEJIDAD_BOOKMARK);
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(complexitivity));
//			StringBuilder sb = new StringBuilder();
//			sb.append("<table:table table:name='Table_Allocation_").append(name).append("' table:style-name='TableGraphic'>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
//			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
//			// Header row
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
//			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 1
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 2
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			// Row 3
//			sb.append("<table:table-row>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
//			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
//			sb.append("</table:table-cell>");
//			sb.append("</table:table-row>");
//			sb.append("</table:table>");
			StringBuilder sb = generateTablePercentajeAndPortals(header1, header2, columna1, columna2, columna3, name, results);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASPUNTUACIONCOMPLEJIDAD_BOOKMARK);
			appendParagraphToMarker(odt, odfFileContent, TABLASPUNTUACIONCOMPLEJIDAD_BOOKMARK);
		}
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionPuntuacionByVerificationLevel1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_1_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		replaceText(odt, odfFileContent, "-441.t1.b2-", labelsL1.get(0).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b3-", labelsL1.get(1).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b4-", labelsL1.get(2).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b5-", labelsL1.get(3).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b6-", labelsL1.get(4).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b7-", labelsL1.get(5).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b8-", labelsL1.get(6).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b9-", labelsL1.get(7).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b10-", labelsL1.get(8).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b11-", labelsL1.get(9).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b12-", labelsL1.get(10).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b13-", labelsL1.get(11).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b14-", labelsL1.get(12).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b15-", labelsL1.get(13).getValue());
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionPuntuacionByVerificationLevel2(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_2_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		replaceText(odt, odfFileContent, "-442.t1.b2-", labelsL2.get(0).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b3-", labelsL2.get(1).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b4-", labelsL2.get(2).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b5-", labelsL2.get(3).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b6-", labelsL2.get(4).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b7-", labelsL2.get(5).getValue());
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionModalityByVerificationLevel1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results1);
		replaceText(odt, odfFileContent, "-451.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c7-", res.get(5).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b8-", res.get(6).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c8-", res.get(6).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b9-", res.get(7).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c9-", res.get(7).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b10-", res.get(8).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c10-", res.get(8).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b11-", res.get(9).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c11-", res.get(9).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b12-", res.get(10).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c12-", res.get(10).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b13-", res.get(11).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c13-", res.get(11).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b14-", res.get(12).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c14-", res.get(12).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b15-", res.get(13).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c15-", res.get(13).getRedPercentage());
	}

	/**
	 * Replace section segment compilance by verification level 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @throws Exception the exception
	 */
	private void replaceSectionCompilanceByVerificationLevel1Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_1);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils
					.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
			String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_1_NAME);
			replaceImageGeneric(odt, graphicPath + graphicName + graphicSuffix + JPG_EXTENSION, graphicName, IMAGE_JPEG);
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c7-", labels.get(5).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b8-", labels.get(6).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c8-", labels.get(6).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b9-", labels.get(7).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c9-", labels.get(7).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b10-", labels.get(8).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c10-", labels.get(8).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b11-", labels.get(9).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c11-", labels.get(9).getRedPercentage());
			// nUEVOS
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b12-", labels.get(10).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c12-", labels.get(10).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b13-", labels.get(11).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c13-", labels.get(11).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b14-", labels.get(12).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c14-", labels.get(12).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.b15-", labels.get(13).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "51.t1.c15-", labels.get(13).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
		}
	}

	/**
	 * Replace section segment compilance by verification level 2.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionCompilanceByVerificationLevel2Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_2);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils
					.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
			String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_BY_VERIFICATION_LEVEL_2_NAME);
			replaceImageGeneric(odt, graphicPath + graphicName + graphicSuffix + JPG_EXTENSION, graphicName, IMAGE_JPEG);
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "52.t1.c7-", labels.get(5).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
		}
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionModalityByVerificationLevel2(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_2_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results2);
		replaceText(odt, odfFileContent, "-452.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c7-", res.get(5).getRedPercentage());
	}

	/**
	 * Replace section compilance by verification level 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionCompilanceByVerificationLevel1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils
				.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
		replaceText(odt, odfFileContent, "-451c.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c7-", res.get(5).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b8-", res.get(6).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c8-", res.get(6).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b9-", res.get(7).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c9-", res.get(7).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b10-", res.get(8).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c10-", res.get(8).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b11-", res.get(9).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c11-", res.get(9).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b12-", res.get(10).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c12-", res.get(10).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b13-", res.get(11).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c13-", res.get(11).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b14-", res.get(12).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c14-", res.get(12).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b15-", res.get(13).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c15-", res.get(13).getRedPercentage());
	}

	/**
	 * Replace section compilance by verification level 2.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionCompilanceByVerificationLevel2(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils
				.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
		replaceText(odt, odfFileContent, "-452c.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c7-", res.get(5).getRedPercentage());
	}

	/**
	 * Reolace section comparision by aspects.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionComparisionAspects(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_ASPECT_MID_NAME);
		replaceImg(odt, graphicPath + graphicName + JPG_EXTENSION, IMAGE_JPEG);
		final Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
		final List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoAspectMidsComparison(messageResources, result);
		replaceText(odt, odfFileContent, "-46.t1.b2-", labels.get(0).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b3-", labels.get(1).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b4-", labels.get(2).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b5-", labels.get(3).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b6-", labels.get(4).getValue());
	}

	/**
	 * Replace section cat 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private void replaceSectionAllocationDistributionGrouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
			final List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalAccessibilityLevel(messageResources, resultsMap);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", graphicSuffix) + JPG_EXTENSION,
					messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", EMPTY_STRING), IMAGE_JPEG);
			replaceText(odt, odfFileContent, "--nv.acc.percent.aa--", labelValueBean.get(0).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.acc.num.aa--", labelValueBean.get(0).getNumberP());
			replaceText(odt, odfFileContent, "--nv.acc.percent.a--", labelValueBean.get(1).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.acc.num.a--", labelValueBean.get(1).getNumberP());
			replaceText(odt, odfFileContent, "--nv.acc.percent.nv--", labelValueBean.get(2).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.acc.num.nv--", labelValueBean.get(2).getNumberP());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
	}

	/**
	 * Replace section segment compilande distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCompilandeDistributionGrouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteCompilance(pageExecutionList);
			final List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalCompilanceLevel(messageResources, resultsMap);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.accessibility.level.compilance.segment.name", graphicSuffix) + JPG_EXTENSION,
					messageResources.getMessage("observatory.graphic.accessibility.level.compilance.segment.name", EMPTY_STRING), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "--nv.con.percent.aa--", labelValueBean.get(0).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.con.num.aa--", labelValueBean.get(0).getNumberP());
			replaceText(odt, odfFileContent, "--nv.con.percent.a--", labelValueBean.get(1).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.con.num.a--", labelValueBean.get(1).getNumberP());
			replaceText(odt, odfFileContent, "--nv.con.percent.nv--", labelValueBean.get(2).getPercentageP());
			replaceText(odt, odfFileContent, "--nv.con.num.nv--", labelValueBean.get(2).getNumberP());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section cat 2.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionAllocationPuntuactionGrouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", graphicSuffix) + JPG_EXTENSION,
					messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", EMPTY_STRING), IMAGE_JPEG);
			numImg++;
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionMidAllocationLevel1Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
		if (!pageExecutionList.isEmpty()) {
			final List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_1_NAME) + graphicSuffix + JPG_EXTENSION,
					messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_1_NAME), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b2-", labelsL1.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b3-", labelsL1.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b4-", labelsL1.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b5-", labelsL1.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b6-", labelsL1.get(4).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b7-", labelsL1.get(5).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b8-", labelsL1.get(6).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b9-", labelsL1.get(7).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b10-", labelsL1.get(8).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b11-", labelsL1.get(9).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b12-", labelsL1.get(10).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b13-", labelsL1.get(11).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b14-", labelsL1.get(12).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b15-", labelsL1.get(13).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionMidAllocationLevel2Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
		if (!pageExecutionList.isEmpty()) {
			final List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_2_NAME) + graphicSuffix + JPG_EXTENSION,
					messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_MID_COMPARATION_LEVEL_2_NAME), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b2-", labelsL2.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b3-", labelsL2.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b4-", labelsL2.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b5-", labelsL2.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b6-", labelsL2.get(4).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b7-", labelsL2.get(5).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionModalityLevel1Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results1);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME) + graphicSuffix + JPG_EXTENSION,
					messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c7-", labels.get(5).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b8-", labels.get(6).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c8-", labels.get(6).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b9-", labels.get(7).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c9-", labels.get(7).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b10-", labels.get(8).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c10-", labels.get(8).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b11-", labels.get(9).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c11-", labels.get(9).getRedPercentage());
			// nUEVOS
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b12-", labels.get(10).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c12-", labels.get(10).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b13-", labels.get(11).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c13-", labels.get(11).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b14-", labels.get(12).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c14-", labels.get(12).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b15-", labels.get(13).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c15-", labels.get(13).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionModalityLevel2Grouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final String graphicSuffix, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results2);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_2_NAME) + graphicSuffix + JPG_EXTENSION,
					messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_2_NAME), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c7-", labels.get(5).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution suitability level.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionSuitabilityLevel(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioUNEEN2019Utils.getEvolutionObservatoriesSitesByType(observatoryId, executionId,
					pageExecutionList);
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A, df);
			final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA, df);
			final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "101.t1", resultDataAA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "102.t1", resultDataA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "103.t1", resultDataNV, true);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			numImg++;
		}
		return numImg;
	}

	/**
	 * TODO.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionComplianceLevel(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioUNEEN2019Utils.getEvolutionObservatoriesSitesByCompliance(observatoryId, executionId,
					pageExecutionList);
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResult, Constants.OBS_COMPILANCE_PARTIAL, df);
			final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResult, Constants.OBS_COMPILANCE_FULL, df);
			final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResult, Constants.OBS_COMPILANCE_NONE, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "111.t1", resultDataAA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "112.t1", resultDataA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, "113.t1", resultDataNV, true);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution average score.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionAverageScore(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateEvolutionPuntuationDataSet(pageExecutionList);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "4.t1", resultData);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by verification.
	 *
	 * @param messageResources   the message resources
	 * @param odt                the odt
	 * @param odfFileContent     the odf file content
	 * @param graphicPath        the graphic path
	 * @param pageObservatoryMap the page observatory map
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreByVerification(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			// TABLA 1
			Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION,
					pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "5.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "6.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.3") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "7.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.4") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "8.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.5") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "9.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.6") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "10.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.7") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "11.t1", resultData);
			// Reorganización de los datos
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.8") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "12.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.9") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "13.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.10") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "14.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.11") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "15.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.12") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "16.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.13") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "17.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.14") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "18.t1", resultData);
			// TABLA 2
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "19.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "20.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.3") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "21.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.4") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "22.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.5") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "23.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.6") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "24.t1", resultData);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			for (int i = 5; i < 25; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by aspect.
	 *
	 * @param messageResources the message resources
	 * @param odt              the odt
	 * @param odfFileContent   the odf file content
	 * @param graphicPath      the graphic path
	 * @param resultsByAspect  the results by aspect
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreByAspect(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, Map<String, BigDecimal>> resultsByAspect) throws Exception {
		if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.general"),
					resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID) + JPG_EXTENSION,
					IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "25.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.presentation"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID) + JPG_EXTENSION,
					IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "26.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.structure"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID) + JPG_EXTENSION,
					IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "27.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.navigation"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID) + JPG_EXTENSION,
					IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "28.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.alternatives"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + JPG_EXTENSION,
					IMAGE_JPEG);
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "29.t1", resultData);
		} else {
			PropertiesManager pmgr = new PropertiesManager();
			for (int i = 25; i < 30; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by segment.
	 *
	 * @param messageResources the message resources
	 * @param odt              the odt
	 * @param odfFileContent   the odf file content
	 * @param graphicPath      the graphic path
	 * @param resultsByAspect  the results by aspect
	 * @param categories       the categories
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreBySegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, Map<String, BigDecimal>> resultsByAspect, final List<CategoriaForm> categories) throws Exception {
		if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			int n = 25;
			for (CategoriaForm category : categories) {
				Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateSegmentEvolutionPuntuationDataSet(category.getName(), resultsByAspect, df);
				replaceImg(odt, graphicPath + category.getName() + JPG_EXTENSION, IMAGE_JPEG);
				numImg++;
				replaceEvolutionTextCellTables(odt, odfFileContent, numSection + EMPTY_STRING + n + ".t1", resultData);
				n++;
			}
		} else {
			PropertiesManager pmgr = new PropertiesManager();
			for (int i = 25; i < 30; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section cat 5.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionAspectsGrouped(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
			final List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoAspectMidsComparison(messageResources, result);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage(OBSERVATORY_GRAPHIC_ASPECT_MID_NAME) + graphicSuffix + JPG_EXTENSION,
					messageResources.getMessage(OBSERVATORY_GRAPHIC_ASPECT_MID_NAME), IMAGE_JPEG);
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "6.t1.b2-", labels.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "6.t1.b3-", labels.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "6.t1.b4-", labels.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "6.t1.b5-", labels.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "6.t1.b6-", labels.get(4).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
			numImg++;
		}
		return numImg;
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplate() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common"));
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateCategories() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common.category"));
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateComplexities() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common.complexities"));
	}

	/**
	 * Merge styles to primary doc.
	 *
	 * @param primaryDoc   the primary doc
	 * @param secondaryDoc the secondary doc
	 * @param suffix       the suffix
	 * @throws Exception the exception
	 */
	private static void mergeStylesToPrimaryDoc(OdfTextDocument primaryDoc, OdfTextDocument secondaryDoc, String suffix) throws Exception {
		OdfFileDom primaryContentDom = primaryDoc.getContentDom();
		OdfOfficeAutomaticStyles primaryDocAutomaticStyles = primaryDoc.getContentDom().getAutomaticStyles();
		OdfOfficeAutomaticStyles secondaryDocAutomaticStyles = secondaryDoc.getContentDom().getAutomaticStyles();
		// Adopt style nodes from secondary doc
		for (int i = 0; i < secondaryDocAutomaticStyles.getLength(); i++) {
			Node style = secondaryDocAutomaticStyles.item(i).cloneNode(true);
			primaryDocAutomaticStyles.appendChild(primaryContentDom.adoptNode(style));
		}
	}

	/**
	 * Rename styles.
	 *
	 * @param doc            the doc
	 * @param odfFileContent the odf file content
	 * @param suffix         the suffix
	 * @throws Exception the exception
	 */
	private static void renameStyles(final OdfTextDocument doc, final OdfFileDom odfFileContent, String suffix) throws Exception {
		OdfOfficeAutomaticStyles docStyles = odfFileContent.getAutomaticStyles();
		// Adopt style nodes from secondary doc
		for (int i = 0; i < docStyles.getLength(); i++) {
			OdfStyleBase style = (OdfStyleBase) docStyles.item(i);
			if (style.hasAttributes() && "paragraph".equals(style.getAttribute("style:family")) && ("h1".equalsIgnoreCase(style.getAttribute("style:parent-style-name"))
					|| "h2".equalsIgnoreCase(style.getAttribute("style:parent-style-name")) || "h3".equalsIgnoreCase(style.getAttribute("style:parent-style-name")))) {
				String prevousName = style.getAttribute("style:name");
				String newName = prevousName + suffix;
				style.setAttribute("style:name", newName);
				final XPath xpath = doc.getXPath();
				final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//text:h[@text:style-name = '%s']", prevousName), odfFileContent, XPathConstants.NODESET);
				// Make the change on the selected nodes
				for (int idx = 0; idx < nodeList.getLength(); idx++) {
					OdfElement node = (OdfElement) nodeList.item(idx);
					node.setAttribute("text:style-name", newName);
//					if (node.getParentNode() != null && node.getParentNode().getParentNode() != null) {
//						((OdfElement) node.getParentNode().getParentNode()).setAttribute("style-name", "LFO3");
//					}
				}
			}
		}
	}

	/**
	 * Merge font types to primary doc.
	 *
	 * @param primaryDoc   the primary doc
	 * @param secondaryDoc the secondary doc
	 * @throws Exception the exception
	 */
	private static void mergeFontTypesToPrimaryDoc(OdfTextDocument primaryDoc, OdfTextDocument secondaryDoc) throws Exception {
		// Insert referenced font types that are not in the primary document you are merging into
		NodeList sdDomNodes = secondaryDoc.getContentDom().getChildNodes().item(0).getChildNodes();
		NodeList pdDomNodes = primaryDoc.getContentDom().getChildNodes().item(0).getChildNodes();
		OdfFileDom primaryContentDom = primaryDoc.getContentDom();
		Node sdFontNode = null;
		Node pdFontNode = null;
		for (int i = 0; i < sdDomNodes.getLength(); i++) {
			if (sdDomNodes.item(i).getNodeName().equals("office:font-face-decls")) {
				sdFontNode = sdDomNodes.item(i);
				break;
			}
		}
		for (int i = 0; i < pdDomNodes.getLength(); i++) {
			Node n = pdDomNodes.item(i);
			if (n.getNodeName().equals("office:font-face-decls")) {
				pdFontNode = pdDomNodes.item(i);
				break;
			}
		}
		if (sdFontNode != null && pdFontNode != null) {
			NodeList sdFontNodeChildList = sdFontNode.getChildNodes();
			NodeList pdFontNodeChildList = pdFontNode.getChildNodes();
			List<String> fontNames = new ArrayList<String>();
			// Get list of existing fonts in primary doc
			for (int i = 0; i < pdFontNodeChildList.getLength(); i++) {
				NamedNodeMap attributes = pdFontNodeChildList.item(i).getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					if (attributes.item(j).getLocalName().equals("name")) {
						fontNames.add(attributes.item(j).getNodeValue());
					}
				}
			}
			// Check each font in the secondary doc to make sure it gets added if the primary doesn't have it
			for (int i = 0; i < sdFontNodeChildList.getLength(); i++) {
				Node fontNode = sdFontNodeChildList.item(i).cloneNode(true);
				NamedNodeMap attributes = fontNode.getAttributes();
				String fontName = "";
				for (int j = 0; j < attributes.getLength(); j++) {
					if (attributes.item(j).getLocalName().equals("name")) {
						fontName = attributes.item(j).getNodeValue();
						break;
					}
				}
				if (!fontName.equals("") && !fontNames.contains(fontName)) {
					pdFontNode.appendChild(primaryContentDom.adoptNode(fontNode));
				}
			}
		}
	}

	/**
	 * Replace img.
	 *
	 * @param odt          the odt
	 * @param filePath     the file path
	 * @param imageNameOdt the image name odt
	 * @param mymeType     the myme type
	 * @throws Exception the exception
	 */
	protected void replaceImageGeneric(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", imageNameOdt), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			final OdfElement node = (OdfElement) nodeList.item(i);
			// Replace href with filename
			String value = PICTURES_ODT_PATH + f.getName();
			node.setAttribute(XLINK_HREF, value);
			// Replace parent (draw:frame) name with filename
			((OdfElement) node.getParentNode()).setAttribute("draw:name", f.getName());
			insertFileInsideODTFile(odt, value, f, mymeType);
		}
	}

	/**
	 * Adds the image next.
	 *
	 * @param odt           the odt
	 * @param filePath      the file path
	 * @param imageNameOdt  the image name odt
	 * @param mymeType      the myme type
	 * @param prevImageName the prev image name
	 * @throws Exception the exception
	 */
	protected void addImageNext(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType, final String prevImageName) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", prevImageName), odfFileContent, XPathConstants.NODESET);
		String newImageDOm = "<draw:frame draw:style-name='fr4' draw:name='" + imageNameOdt
				+ "' text:anchor-type='as-char' svg:y='0mm' svg:width='149.45mm' style:rel-width='scale' svg:height='120.21mm' style:rel-height='scale' draw:z-index='118'>"
				+ "<draw:image xlink:href='Pictures/" + f.getName() + "' xlink:type='simple' xlink:show='embed' xlink:actuate='onLoad'/>" + "<svg:desc> </svg:desc>" + "</draw:frame>";
		Element newIMage = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(newImageDOm.getBytes())).getDocumentElement();
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newIMage), node.getParentNode().getNextSibling());
			insertFileInsideODTFile(odt, PICTURES_ODT_PATH + f.getName(), f, mymeType);
		}
	}
}
