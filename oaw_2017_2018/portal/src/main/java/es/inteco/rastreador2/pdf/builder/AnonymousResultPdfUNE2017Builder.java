package es.inteco.rastreador2.pdf.builder;

import java.io.File;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;

import es.inteco.common.Constants;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2017Utils;

/**
 * AnonymousResultPdfUNE2017Builder. Clase replicada de
 * {@link AnonymousResultPdfUNE2012Builder} para la nueva versión de la
 * metodología basada en la misma norma que la mencionada y conservar ambas para
 * futuras consultas o comparativas.
 */
public class AnonymousResultPdfUNE2017Builder extends AnonymousResultPdfBuilder {

	/**
	 * Instantiates a new anonymous result pdf UNE 2017 builder.
	 *
	 * @param fileOut
	 *            the file out
	 * @param observatoryType
	 *            the observatory type
	 * @throws Exception
	 *             the exception
	 */
	public AnonymousResultPdfUNE2017Builder(File fileOut, long observatoryType) throws Exception {
		super(fileOut, observatoryType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * generateGraphics(org.apache.struts.util.MessageResources,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, final String observatoryId, String filePath) throws Exception {
		ResultadosAnonimosObservatorioUNE2017Utils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createIntroductionChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font)
	 */
	@Override
	protected void createIntroductionChapter(MessageResources resources, Font titleFont) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createObjetiveChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font)
	 */
	@Override
	protected void createObjetiveChapter(MessageResources resources, Font titleFont) throws DocumentException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createMethodologyChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font, java.util.List, boolean)
	 */
	@Override
	protected void createMethodologyChapter(MessageResources resources, Font titleFont, List<ObservatoryEvaluationForm> primaryReportPageList, boolean isBasicService) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createGlobalResultsChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font, java.lang.String, java.lang.String,
	 * java.util.List)
	 */
	@Override
	protected void createGlobalResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, List<CategoriaForm> categories) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createCategoryResultsChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font, java.lang.String, java.lang.String,
	 * es.inteco.rastreador2.actionform.semillas.CategoriaForm)
	 */
	@Override
	protected void createCategoryResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, CategoriaForm category) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createEvolutionResultsChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	protected void createEvolutionResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String idExecution, String idObservatory) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder#
	 * createSummaryChapter(org.apache.struts.util.MessageResources,
	 * com.lowagie.text.Font)
	 */
	@Override
	protected void createSummaryChapter(MessageResources resources, Font titleFont) throws Exception {
	}

}
