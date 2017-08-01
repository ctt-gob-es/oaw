package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import es.inteco.common.Constants;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;
import org.apache.struts.util.MessageResources;

import java.io.File;
import java.util.List;

/**
 * Clase la generación de los informes anónimos de resultados de la normativa observatorio UNE-2012 en PDF
 */
public class AnonymousResultPdfUNE2012Builder extends  AnonymousResultPdfBuilder {
    public AnonymousResultPdfUNE2012Builder(File fileOut, long observatoryType) throws Exception {
        super(fileOut, observatoryType);
    }

    @Override
    public void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, final String observatoryId, String filePath) throws Exception {
        ResultadosAnonimosObservatorioUNE2012Utils.generateGraphics(messageResources, executionId, idExecutionObservatory, observatoryId, filePath, Constants.MINISTERIO_P, true);
    }

    @Override
    protected void createIntroductionChapter(MessageResources resources, Font titleFont) throws Exception {

    }

    @Override
    protected void createObjetiveChapter(MessageResources resources, Font titleFont) throws DocumentException {

    }

    @Override
    protected void createMethodologyChapter(MessageResources resources, Font titleFont, List<ObservatoryEvaluationForm> primaryReportPageList, boolean isBasicService) throws Exception {

    }

    @Override
    protected void createGlobalResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, List<CategoriaForm> categories) throws Exception {

    }

    @Override
    protected void createCategoryResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, CategoriaForm category) throws Exception {

    }

    @Override
    protected void createEvolutionResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String idExecution, String idObservatory) throws Exception {

    }

    @Override
    protected void createSummaryChapter(MessageResources resources, Font titleFont) throws Exception {

    }
}
