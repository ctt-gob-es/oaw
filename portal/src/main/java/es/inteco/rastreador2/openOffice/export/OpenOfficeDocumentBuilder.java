package es.inteco.rastreador2.openOffice.export;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class OpenOfficeDocumentBuilder {
    protected final String executionId;
    protected final String observatoryId;
    protected final Long tipoObservatorio;

    protected int numImg = 8;
    protected int numSection = 5;

    protected OpenOfficeDocumentBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio) {
        this.executionId = executionId;
        this.observatoryId = observatoryId;
        this.tipoObservatorio = tipoObservatorio;
    }

    public abstract OdfTextDocument buildDocument(HttpServletRequest resources, String graphicPath, String date, boolean evolution, List<ObservatoryEvaluationForm> pageExecutionList, List<CategoriaForm> categories) throws Exception;

    protected void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText, final String nodeStr) throws XPathExpressionException {
        XPath xpath = odt.getXPath();
        DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            OdfElement node = (OdfElement) nodeList.item(i);
            node.setTextContent(node.getTextContent().replace(oldText, newText));
        }
    }

    protected void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText) throws XPathExpressionException {
        replaceText(odt, odfFileContent, oldText, newText, "text:p");
    }

    protected void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData) throws XPathExpressionException {
        replaceEvolutionTextCellTables(odt, odfFileContent, rowId, resultData, false);
    }

    protected void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData, final boolean percentValue) throws XPathExpressionException {
        int index = 2;
        for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
            final String value = getCellValue(entry.getValue(), percentValue);
            replaceText(odt, odfFileContent, "-" + rowId + ".a" + index + "-", entry.getKey());
            replaceText(odt, odfFileContent, "-" + rowId + ".b" + index++ + "-", value);
        }
        // Para el resto de la tabla borramos los placeholders para que al menos las celdas salgan vacías
        while (index <= 6) {
            replaceText(odt, odfFileContent, "-" + rowId + ".a" + index + "-", "");
            replaceText(odt, odfFileContent, "-" + rowId + ".b" + index++ + "-", "");
        }
    }

    protected String getCellValue(final BigDecimal value, final boolean percentValue) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return "NP";
        } else {
            return value.toString() + (percentValue ? "%" : "");
        }
    }

    protected void replaceImg(final OdfTextDocument odt, final String newImagePath, final String mymeType) throws Exception {
        final File f = new File(newImagePath);
        final String embededName = getEmbededIdImage(tipoObservatorio, f.getName());
        FileInputStream fin = null;

        try {
            fin = new FileInputStream(newImagePath);
            odt.getPackage().insert(fin, "Pictures/" + embededName, mymeType);
        } catch (Exception e) {
            Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

    protected abstract String getEmbededIdImage(Long tipoObservatorio, String name);

}
