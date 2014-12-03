package es.inteco.rastreador2.openOffice.export;

import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import org.apache.struts.util.MessageResources;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface OpenOfficeDocumentBuilder {

    public OdfTextDocument buildDocument(HttpServletRequest resources, String graphicPath, String date, boolean evolution, List<ObservatoryEvaluationForm> pageExecutionList, List<CategoriaForm> categories) throws Exception;

}
