package es.inteco.rastreador2.pdf.builder;

import java.io.File;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;

import es.inteco.common.Constants;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;

/**
 * Clase la generación de los informes anónimos de resultados de la normativa observatorio UNE-2012 en PDF.
 */
public class AnonymousResultPdfUNE2012Builder extends AnonymousResultPdfBuilder {
	/**
	 * Instantiates a new anonymous result pdf UNE 2012 builder.
	 *
	 * @param fileOut         the file out
	 * @param observatoryType the observatory type
	 * @throws Exception the exception
	 */
	public AnonymousResultPdfUNE2012Builder(File fileOut, long observatoryType) throws Exception {
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
	@Override
	public void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, final String observatoryId, String filePath) throws Exception {
		ResultadosAnonimosObservatorioUNE2012Utils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
	}

	/**
	 * Creates the introduction chapter.
	 *
	 * @param resources the resources
	 * @param titleFont the title font
	 * @throws Exception the exception
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
		ResultadosAnonimosObservatorioUNE2012Utils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
	}
}
