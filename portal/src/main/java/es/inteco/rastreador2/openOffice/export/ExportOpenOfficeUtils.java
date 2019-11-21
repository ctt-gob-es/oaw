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
package es.inteco.rastreador2.openOffice.export;

import static es.inteco.common.Constants.PDF_PROPERTIES;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;

/**
 * The Class ExportOpenOfficeUtils.
 */
public final class ExportOpenOfficeUtils {
	/**
	 * Instantiates a new export open office utils.
	 */
	private ExportOpenOfficeUtils() {
	}

	/**
	 * Creates the open office document.
	 *
	 * @param request                     the request
	 * @param filePath                    the file path
	 * @param graphicPath                 the graphic path
	 * @param date                        the date
	 * @param tipoObservatorio            the tipo observatorio
	 * @param numberObservatoryExecutions the number observatory executions
	 */
	public static void createOpenOfficeDocument(final HttpServletRequest request, final String filePath, final String graphicPath, final String date, final Long tipoObservatorio,
			int numberObservatoryExecutions) {
		final long idObservatory;
		if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
			idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		} else {
			idObservatory = 0;
		}
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID),
					Constants.COMPLEXITY_SEGMENT_NONE, null);
			final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, Long.valueOf(request.getParameter(Constants.ID)));
			final OpenOfficeDocumentBuilder openOfficeDocumentBuilder = getDocumentBuilder(request.getParameter(Constants.ID), request.getParameter(Constants.ID_OBSERVATORIO), tipoObservatorio,
					CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId()));
			final OdfTextDocument odt = openOfficeDocumentBuilder.buildDocument(request, graphicPath, date, includeEvolution(numberObservatoryExecutions), pageExecutionList, categories);
			odt.save(filePath);
			removeAttributeFromFile(filePath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");
			odt.close();
		} catch (Exception e) {
			Logger.putLog("Error al exportar los resultados a OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Creates the open office document filtered.
	 *
	 * @param request                     the request
	 * @param filePath                    the file path
	 * @param graphicPath                 the graphic path
	 * @param date                        the date
	 * @param tipoObservatorio            the tipo observatorio
	 * @param numberObservatoryExecutions the number observatory executions
	 */
	public static void createOpenOfficeDocumentFiltered(final HttpServletRequest request, final String filePath, final String graphicPath, final String date, final Long tipoObservatorio,
			int numberObservatoryExecutions, String[] tagsToFilter, Map<String, Boolean> grpahicConditional, String[] exObsIds, Long idBaseTemplate, Long idSegmentTemplate, Long idComplexityTemplate) {
		final long idObservatory;
		if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
			idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		} else {
			idObservatory = 0;
		}
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);

			//TODO Apply tags filter
			final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(request.getParameter(Constants.ID),
					Constants.COMPLEXITY_SEGMENT_NONE, null, false, tagsToFilter);

			final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, Long.valueOf(request.getParameter(Constants.ID)));
			final OpenOfficeDocumentBuilder openOfficeDocumentBuilder = getDocumentBuilder(request.getParameter(Constants.ID), request.getParameter(Constants.ID_OBSERVATORIO), tipoObservatorio,
					CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId()));
			final OdfTextDocument odt = openOfficeDocumentBuilder.buildDocumentFiltered(request, graphicPath, date, includeEvolution(numberObservatoryExecutions), pageExecutionList, categories,
					tagsToFilter, grpahicConditional, exObsIds, idBaseTemplate, idSegmentTemplate, idComplexityTemplate);
			odt.save(filePath);
			removeAttributeFromFile(filePath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");
			odt.close();
		} catch (Exception e) {
			Logger.putLog("Error al exportar los resultados a OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the document builder.
	 *
	 * @param executionId      the execution id
	 * @param observatoryId    the observatory id
	 * @param tipoObservatorio the tipo observatorio
	 * @param version          the version
	 * @return the document builder
	 */
	private static OpenOfficeDocumentBuilder getDocumentBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio, final String version) {
		if (Constants.NORMATIVA_ACCESIBILIDAD.equals(version)) {
			return new OpenOfficeAccesibilidadBuilder(executionId, observatoryId, tipoObservatorio);
		} else if (Constants.NORMATIVA_UNE_EN2019.equals(version)) {
			return new OpenOfficeUNEEN2019DocumentBuilder(executionId, observatoryId, tipoObservatorio);
		} else if (Constants.NORMATIVA_UNE_2012.equals(version)) {
			return new OpenOfficeUNE2012DocumentBuilder(executionId, observatoryId, tipoObservatorio);
		} else if (Constants.NORMATIVA_UNE_2012_B.equals(version)) {
			return new OpenOfficeUNE2012BDocumentBuilder(executionId, observatoryId, tipoObservatorio);
		} else {
			return new OpenOfficeUNE2004DocumentBuilder(executionId, observatoryId, tipoObservatorio);
		}
	}

	/**
	 * Include evolution.
	 *
	 * @param numObs the num obs
	 * @return true, if successful
	 */
	private static boolean includeEvolution(final int numObs) {
		final PropertiesManager pmgr = new PropertiesManager();
		return numObs >= Integer.parseInt(pmgr.getValue(PDF_PROPERTIES, "pdf.anonymous.results.pdf.min.obser"));
	}

	/**
	 * Removes the attribute from file.
	 *
	 * @param doc       the doc
	 * @param xmlFile   the xml file
	 * @param node      the node
	 * @param attribute the attribute
	 * @param mymeType  the myme type
	 * @throws Exception the exception
	 */
	private static void removeAttributeFromFile(final String doc, final String xmlFile, final String node, final String attribute, final String mymeType) throws Exception {
		final OdfPackage odfPackageNew = OdfPackage.loadPackage(doc);
		final Document packageDocument = odfPackageNew.getDom(xmlFile);
		final NodeList nodeList = packageDocument.getElementsByTagName(node);
		for (int i = 0; i < nodeList.getLength(); i++) {
			((Element) nodeList.item(i)).removeAttribute(attribute);
		}
		odfPackageNew.insert(packageDocument, xmlFile, mymeType);
		odfPackageNew.save(doc);
		odfPackageNew.close();
	}
}
