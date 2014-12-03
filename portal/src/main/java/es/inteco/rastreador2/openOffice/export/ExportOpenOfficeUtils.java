package es.inteco.rastreador2.openOffice.export;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.Constants.PDF_PROPERTIES;

public final class ExportOpenOfficeUtils {

    private ExportOpenOfficeUtils() {
    }

    public static void createOpenOfficeDocument(HttpServletRequest request, String filePath, String graphicPath, String date, Long tipoObservatorio, int numObs) {
        long idObservatory = 0;
        if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
            idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
        }
        final Connection c = DataBaseManager.getConnection();
        try {
            final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID), Constants.COMPLEXITY_SEGMENT_NONE, null);
            final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, Long.valueOf(request.getParameter(Constants.ID)));

            final OpenOfficeDocumentBuilder openOfficeDocumentBuilder = getDocumentBuilder(request.getParameter(Constants.ID),request.getParameter(Constants.ID_OBSERVATORIO), tipoObservatorio, CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId()));
            final OdfTextDocument odt = openOfficeDocumentBuilder.buildDocument(request, graphicPath, date, includeEvolution(numObs), pageExecutionList, categories);

            odt.save(filePath);

            removeAttributeFromFile(filePath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");

            odt.close();
        } catch (Exception e) {
            Logger.putLog("Error al exportar los resultados a OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

    }

    private static OpenOfficeDocumentBuilder getDocumentBuilder(String executionId, String observatoryId,  Long tipoObservatorio, String version) {
        if ( "UNE-2012".equals(version) ) {
            return new OpenOfficeUNE2012DocumentBuilder(executionId,observatoryId, tipoObservatorio);
        } else {
            return new OpenOfficeUNE2004DocumentBuilder(executionId,observatoryId, tipoObservatorio);
        }
    }

    private static boolean includeEvolution(int numObs) {
        final PropertiesManager pmgr = new PropertiesManager();
        return numObs >= Integer.parseInt(pmgr.getValue(PDF_PROPERTIES, "pdf.anonymous.results.pdf.min.obser"));
    }

    private static void removeAttributeFromFile(String doc, String xmlFile, String node, String attribute, String mymeType) throws Exception {
        OdfPackage odfPackageNew = OdfPackage.loadPackage(doc);
        Document packageDocument = odfPackageNew.getDom(xmlFile);

        NodeList nodeList = packageDocument.getElementsByTagName(node);
        for (int i = 0; i < nodeList.getLength(); i++) {
            ((Element) nodeList.item(i)).removeAttribute(attribute);
        }
        odfPackageNew.insert(packageDocument, xmlFile, mymeType);
        odfPackageNew.save(doc);
        odfPackageNew.close();
    }

}
