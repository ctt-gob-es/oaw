package es.inteco.rastreador2.action.observatorio;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


public final class ObservatoryExport {

    private ObservatoryExport() {
    }

    public static void generateObservatoryPDF(String execution_id, String observatory_id, Locale language,
                                              HttpServletResponse response, HttpServletRequest request, ActionMapping mapping, String graphic) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();

        String pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.inteco.exports")
                + observatory_id + File.separator + execution_id + File.separator + language.getLanguage();

        exportToPdf(pdfPath);
    }

    private static void exportToPdf(String pdfPath) {

        PropertiesManager pmgr = new PropertiesManager();

        // Ruta de la plantilla
        String templatePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.template.export");

        File file = new File(pdfPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        PdfStamper stamp = null;
        PdfReader reader = null;

        try {
            //Rellenamos la plantilla
            reader = new PdfReader(templatePath);
            stamp = new PdfStamper(reader, new FileOutputStream(pdfPath));

            AcroFields informeForm = stamp.getAcroFields();

            //Aqui añadimos las gráficas a la plantilla
            //informeForm.setField("fecha_inicio", new String(certificate.getFecha_inicio().getBytes("UTF-8"), "UTF-8"));

            stamp.setFormFlattening(true);

        } catch (Exception e) {
            Logger.putLog("Error al incluir los datos del curso en la plantilla del diploma.", ObservatoryExport.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            closeReader(reader);
            closeStamper(stamp);
        }
    }

    private static void closeReader(PdfReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el Reader.", ObservatoryExport.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    private static void closeStamper(PdfStamper stamper) {
        if (stamper != null) {
            try {
                stamper.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el Stamper.", ObservatoryExport.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }
}
