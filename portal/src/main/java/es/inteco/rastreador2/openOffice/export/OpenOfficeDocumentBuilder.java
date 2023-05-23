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

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;

/**
 * The Class OpenOfficeDocumentBuilder.
 */
public abstract class OpenOfficeDocumentBuilder {
	/** The execution id. */
	protected final String executionId;
	/** The observatory id. */
	protected final String observatoryId;
	/** The tipo observatorio. */
	protected final Long tipoObservatorio;
	/** The num img. */
	protected int numImg = 8;
	/** The num section. */
	protected int numSection = 5;

	/**
	 * Instantiates a new open office document builder.
	 *
	 * @param executionId      the execution id
	 * @param observatoryId    the observatory id
	 * @param tipoObservatorio the tipo observatorio
	 */
	protected OpenOfficeDocumentBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio) {
		this.executionId = executionId;
		this.observatoryId = observatoryId;
		this.tipoObservatorio = tipoObservatorio;
	}

	/**
	 * Builds the document.
	 *
	 * @param resources         the resources
	 * @param graphicPath       the graphic path
	 * @param date              the date
	 * @param evolution         the evolution
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @return the odf text document
	 * @throws Exception the exception
	 */
	public abstract OdfTextDocument buildDocument(HttpServletRequest resources, String graphicPath, String date, boolean evolution, List<ObservatoryEvaluationForm> pageExecutionList,
			List<CategoriaForm> categories) throws Exception;

	/**
	 * Replace text.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param oldText        the old text
	 * @param newText        the new text
	 * @param nodeStr        the node str
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText, final String nodeStr) throws XPathExpressionException {
		XPath xpath = odt.getXPath();
		DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.setTextContent(node.getTextContent().replace(oldText, newText));
		}
	}

	/**
	 * Replace text.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param oldText        the old text
	 * @param newText        the new text
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText) throws XPathExpressionException {
		if (oldText != null && newText != null) {
			replaceText(odt, odfFileContent, oldText, newText, "text:p");
		}
	}

	/**
	 * Replace evolution text cell tables.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param rowId          the row id
	 * @param resultData     the result data
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData)
			throws XPathExpressionException {
		replaceEvolutionTextCellTables(odt, odfFileContent, rowId, resultData, false);
	}

	/**
	 * Replace evolution text cell tables.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param rowId          the row id
	 * @param resultData     the result data
	 * @param isPercentValue the is percent value
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData,
			final boolean isPercentValue) throws XPathExpressionException {
		int index = 2;
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			replaceText(odt, odfFileContent, "-" + rowId + ".a" + index + "-", entry.getKey());
			replaceText(odt, odfFileContent, "-" + rowId + ".b" + index + "-", getCellValue(entry.getValue(), isPercentValue));
			index++;
		}
		// Para el resto de la tabla borramos los placeholders para que al menos las celdas salgan vacías
		while (index <= 7) {
			replaceText(odt, odfFileContent, "-" + rowId + ".a" + index + "-", "");
			replaceText(odt, odfFileContent, "-" + rowId + ".b" + index + "-", "");
			index++;
		}
	}

	/**
	 * Replace evolution compliance text cell tables.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param rowId          the row id
	 * @param resultData     the result data
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceEvolutionComplianceTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, Map<String, BigDecimal>> resultData)
			throws XPathExpressionException {
		replaceEvolutionComplianceTextCellTables(odt, odfFileContent, rowId, resultData, true);
	}

	/**
	 * Replace evolution compliance text cell tables.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param rowId          the row id
	 * @param resultData     the result data
	 * @param isPercentValue the is percent value
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceEvolutionComplianceTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, Map<String, BigDecimal>> resultData,
			final boolean isPercentValue) throws XPathExpressionException {
		int index = 2;
		for (Map.Entry<String, Map<String, BigDecimal>> entry : resultData.entrySet()) {
			// Necesitamos reordenar los resputados para que el valor 1.10
			// vaya después de 1.9 y no de 1.1
			Map<String, BigDecimal> results = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String version1, String version2) {
					String[] v1 = version1.split("\\.");
					String[] v2 = version2.split("\\.");
					int major1 = major(v1);
					int major2 = major(v2);
					if (major1 == major2) {
						if (minor(v1) == minor(v2)) { // Devolvemos 1
														// aunque sean iguales
														// porque las claves lleva
														// asociado un subfijo que
														// aqui no tenemos en cuenta
							return 1;
						}
						return minor(v1).compareTo(minor(v2));
					}
					return major1 > major2 ? 1 : -1;
				}

				private int major(String[] version) {
					return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
							.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
				}

				private Integer minor(String[] version) {
					return version.length > 1 ? Integer.parseInt(version[1].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
							.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "")) : 0;
				}
			});
			for (Map.Entry<String, BigDecimal> entryU : entry.getValue().entrySet()) {
				results.put(entryU.getKey(), entryU.getValue());
			}
			for (Map.Entry<String, BigDecimal> resultC : results.entrySet()) {
				// Cabecera
				String vCount = resultC.getKey().substring(resultC.getKey().lastIndexOf(".") + 1, resultC.getKey().indexOf("_"));
				replaceText(odt, odfFileContent, "-" + rowId + vCount + ".a" + index + "-", entry.getKey());
				final String oldTextC = "-" + rowId + vCount + ".b" + index + ".c-";
				final String oldTextNC = "-" + rowId + vCount + ".b" + index + ".nc-";
				final String oldTextNA = "-" + rowId + vCount + ".b" + index + ".na-";
				// final String oldTextNA = "-" + rowId + vCount + ".b" + index + ".na-";
				if (resultC.getKey().endsWith(Constants.OBS_VALUE_COMPILANCE_SUFFIX)) {
					replaceText(odt, odfFileContent, oldTextC, getCellValue(resultC.getValue(), isPercentValue));
				} else if (resultC.getKey().endsWith(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX)) {
					replaceText(odt, odfFileContent, oldTextNC, getCellValue(resultC.getValue(), isPercentValue));
				}
				// NOT APLLY
				else if (resultC.getKey().endsWith(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX)) {
					replaceText(odt, odfFileContent, oldTextNA, getCellValue(resultC.getValue(), isPercentValue));
				}
			}
			index++;
		}
		// Para el resto de la tabla borramos los placeholders para que al menos las celdas salgan vacías
		while (index <= 7) {
			for (int i = 0; i <= 14; i++) {
				replaceText(odt, odfFileContent, "-" + rowId + i + ".a" + index + "-", "");
				replaceText(odt, odfFileContent, "-" + rowId + i + ".b" + index + ".c-", "");
				replaceText(odt, odfFileContent, "-" + rowId + i + ".b" + index + ".nc-", "");
				replaceText(odt, odfFileContent, "-" + rowId + i + ".b" + index + ".na-", "");
			}
			index++;
		}
	}

	/**
	 * Gets the cell value.
	 *
	 * @param value          the value
	 * @param isPercentValue the is percent value
	 * @return the cell value
	 */
	protected String getCellValue(final BigDecimal value, final boolean isPercentValue) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
			return "NP";
		} else {
			return value.toString() + (isPercentValue ? "%" : "");
		}
	}

	/**
	 * Replace img.
	 *
	 * @param odt          the odt
	 * @param newImagePath the new image path
	 * @param mymeType     the myme type
	 * @throws Exception the exception
	 */
	protected void replaceImg(final OdfTextDocument odt, final String newImagePath, final String mymeType) throws Exception {
		final File f = new File(newImagePath);
		final String imageName = f.getName().substring(0, f.getName().length() - 4);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", imageName), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			final OdfElement node = (OdfElement) nodeList.item(i);
			final String embededName = node.getAttribute("xlink:href").trim();
			insertFileInsideODTFile(odt, embededName, f, mymeType);
		}
		final String embededName = getEmbededIdImage(tipoObservatorio, f.getName());
		insertFileInsideODTFile(odt, "Pictures/" + embededName, f, mymeType);
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
	protected abstract String getEmbededIdImage(Long tipoObservatorio, String name);

	/**
	 * Builds the document filtered.
	 *
	 * @param request               the request
	 * @param filePath              the file path
	 * @param graphicPath           the graphic path
	 * @param date                  the date
	 * @param evolution             the evolution
	 * @param pageExecutionList     the page execution list
	 * @param categories            the categories
	 * @param tagsToFilter          the tags to filter
	 * @param tagsToFilterFixed     the tags to filter fixed
	 * @param grpahicConditional    the grpahic conditional
	 * @param exObsIds              the ex obs ids
	 * @param idBaseTemplate        the id base template
	 * @param idSegmentTemplate     the id segment template
	 * @param idComplexityTemplate  the id complexity template
	 * @param idSegmentEvolTemplate the id segment evol template
	 * @param reportTitle           the report title
	 * @return the odf text document odf text document
	 * @throws Exception the exception
	 */
	public abstract OdfTextDocument buildDocumentFiltered(final HttpServletRequest request, final String filePath, final String graphicPath, final String date, final boolean evolution,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final String[] tagsToFilter, final String[] tagsToFilterFixed,
			final Map<String, Boolean> grpahicConditional, final String[] exObsIds, final Long idBaseTemplate, final Long idSegmentTemplate, final Long idComplexityTemplate,
			final Long idSegmentEvolTemplate, final String reportTitle) throws Exception;
}
