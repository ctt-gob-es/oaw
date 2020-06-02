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

import java.io.File;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

import es.inteco.common.Constants;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012BUtils;

/**
 * AnonymousResultPdfUNE2012bBuilder. Clase replicada de {@link AnonymousResultPdfUNE2012Builder} para la nueva versión de la metodología basada en la misma norma que la mencionada y conservar ambas
 * para futuras consultas o comparativas.
 */
public class AnonymousResultPdfUNE2012bBuilder extends AnonymousResultPdfBuilder {
	/**
	 * Instantiates a new anonymous result pdf UNE 2012b builder.
	 *
	 * @param fileOut         the file out
	 * @param observatoryType the observatory type
	 * @throws Exception the exception
	 */
	public AnonymousResultPdfUNE2012bBuilder(File fileOut, long observatoryType) throws Exception {
		super(fileOut, observatoryType);
	}

	/**
	 * Generate graphics.
	 *
	 * @param messageResources       the message resources
	 * @param executionId            the execution id
	 * @param idExecutionObservatory the id execution observatory
	 * @param observatoryId          the observatory id
	 * @param filePath               the file path
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# generateGraphics(org.apache.struts.util.MessageResources, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, final String observatoryId, String filePath) throws Exception {
		ResultadosAnonimosObservatorioUNE2012BUtils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
	}

	/**
	 * Creates the introduction chapter.
	 *
	 * @param resources the resources
	 * @param titleFont the title font
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createIntroductionChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font)
	 */
	@Override
	protected void createIntroductionChapter(MessageResources resources, Font titleFont) throws Exception {
	}

	/**
	 * Creates the objetive chapter.
	 *
	 * @param resources the resources
	 * @param titleFont the title font
	 * @throws DocumentException the document exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createObjetiveChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font)
	 */
	@Override
	protected void createObjetiveChapter(MessageResources resources, Font titleFont) throws DocumentException {
	}

	/**
	 * Creates the methodology chapter.
	 *
	 * @param resources             the resources
	 * @param titleFont             the title font
	 * @param primaryReportPageList the primary report page list
	 * @param isBasicService        the is basic service
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createMethodologyChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font, java.util.List, boolean)
	 */
	@Override
	protected void createMethodologyChapter(MessageResources resources, Font titleFont, List<ObservatoryEvaluationForm> primaryReportPageList, boolean isBasicService) throws Exception {
	}

	/**
	 * Creates the global results chapter.
	 *
	 * @param resources    the resources
	 * @param titleFont    the title font
	 * @param graphicPath  the graphic path
	 * @param execution_id the execution id
	 * @param categories   the categories
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createGlobalResultsChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font, java.lang.String, java.lang.String,
	 * java.util.List)
	 */
	@Override
	protected void createGlobalResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, List<CategoriaForm> categories) throws Exception {
	}

	/**
	 * Creates the category results chapter.
	 *
	 * @param resources    the resources
	 * @param titleFont    the title font
	 * @param graphicPath  the graphic path
	 * @param execution_id the execution id
	 * @param category     the category
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createCategoryResultsChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font, java.lang.String,
	 * java.lang.String, es.inteco.rastreador2.actionform.semillas.CategoriaForm)
	 */
	@Override
	protected void createCategoryResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, CategoriaForm category) throws Exception {
	}

	/**
	 * Creates the evolution results chapter.
	 *
	 * @param resources     the resources
	 * @param titleFont     the title font
	 * @param graphicPath   the graphic path
	 * @param idExecution   the id execution
	 * @param idObservatory the id observatory
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createEvolutionResultsChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	protected void createEvolutionResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String idExecution, String idObservatory) throws Exception {
	}

	/**
	 * Creates the summary chapter.
	 *
	 * @param resources the resources
	 * @param titleFont the title font
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder# createSummaryChapter(org.apache.struts.util.MessageResources, com.lowagie.text.Font)
	 */
	@Override
	protected void createSummaryChapter(MessageResources resources, Font titleFont) throws Exception {
	}

	/**
	 * Generate graphics.
	 *
	 * @param messageResources       the message resources
	 * @param executionId            the execution id
	 * @param idExecutionObservatory the id execution observatory
	 * @param observatoryId          the observatory id
	 * @param filePath               the file path
	 * @param tagsFilter             the tags filter
	 * @throws Exception the exception
	 */
	@Override
	public void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, String observatoryId, String filePath, String[] tagsFilter, String[] exObsIds)
			throws Exception {
		ResultadosAnonimosObservatorioUNE2012BUtils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
	}
}
