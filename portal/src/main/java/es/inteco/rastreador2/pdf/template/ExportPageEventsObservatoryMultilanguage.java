package es.inteco.rastreador2.pdf.template;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.pdf.utils.ExportPageEventsUtils;

import java.awt.*;

/**
 * Build the header and footer of the document.
 *
 * @author j.delamo
 * @version 1.0
 */
public class ExportPageEventsObservatoryMultilanguage extends PdfPageEventHelper {

    private boolean basicService = false;
    private static boolean lastPage = false;
    private String footText = "";
    private String date = "";

    /**
     * Marks the last page of the document if it is true.
     *
     * @param lastPageParam Boolean variable that marks the last page.
     */
    public static void setLastPage(final boolean lastPageParam) {
        lastPage = lastPageParam;
    }

    public ExportPageEventsObservatoryMultilanguage(String footText, String date, boolean basicService) {
        this.footText = footText;
        this.date = date;
        this.basicService = basicService;
    }

    /**
     * This is the event for headers and footers.
     *
     * @param w A PdfWriter object.
     * @param d A Document object.
     * @throws Exception
     */

    public final void onEndPage(final PdfWriter w, final Document d) {
        try {
            PropertiesManager pmgr = new PropertiesManager();
            PdfContentByte cb = w.getDirectContent();
            BaseFont texto = BaseFont.createFont(pmgr.getValue(Constants.PDF_PROPERTIES, "path.pdf.font"), BaseFont.CP1252, BaseFont.EMBEDDED);

            if (!basicService) {
                createHeader(cb, texto, d);
            } else {
                createBasicServiceHeader(cb, texto, d);
            }

            if (!lastPage) {
                createFooter(cb, w, texto, d);
            }

        } catch (Exception e) {
            Logger.putLog("Exception", ExportPageEventsObservatoryMultilanguage.class, es.inteco.common.logging.Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private void createHeader(PdfContentByte cb, BaseFont texto, Document d) throws DocumentException {
        PropertiesManager pmgr = new PropertiesManager();
        int posX = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.logo.posX"));
        int posY = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.logo.posY"));

        Image logoMinisterio = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.ministerio.logo"), 158, 45, "Informe de Accesibilidad. Logotipo Ministerio de la Presidencia.");
        if (logoMinisterio != null) {
            logoMinisterio.setAbsolutePosition(posX, posY);
            cb.addImage(logoMinisterio);
            cb.endMarkedContentSequence();
        }

        Image logoObservatorio = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.observatorio.multilanguage.logo"), 233, 45, "Informe de Accesibilidad. Logotipo Observatorio INTECO.");
        if (logoObservatorio != null) {
            logoObservatorio.setAbsolutePosition(d.getPageSize().getWidth() - logoObservatorio.getScaledWidth() - posX, posY);
            cb.addImage(logoObservatorio);
            cb.endMarkedContentSequence();
        }
    }

    private void createBasicServiceHeader(PdfContentByte cb, BaseFont texto, Document d) throws DocumentException {
        PropertiesManager pmgr = new PropertiesManager();
        int posX = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.logo.posX"));
        int posY = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.logo.posY"));

        Image logoMinisterio = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.ministerio.logo"), 158, 45, "Informe de Accesibilidad. Logotipo Ministerio de la Presidencia.");
        if (logoMinisterio != null) {
            logoMinisterio.setAbsolutePosition(posX, posY);
            cb.addImage(logoMinisterio);
            cb.endMarkedContentSequence();
        }

        Image logoObservatorio = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.observatorio.multilanguage.logo"), 233, 45, "Informe de Accesibilidad. Logotipo Observatorio INTECO.");
        if (logoObservatorio != null) {
            logoObservatorio.setAbsolutePosition(posX + logoMinisterio.getScaledWidth(), posY);
            cb.addImage(logoObservatorio);
            cb.endMarkedContentSequence();
        }

        Image logoINTECO = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.inteco.logo"), 70, 45, "Informe de Accesibilidad. Logotipo INTECO.");
        if (logoINTECO != null) {
            logoINTECO.setAbsolutePosition(d.getPageSize().getWidth() - logoINTECO.getScaledWidth() - posX, posY);
            cb.addImage(logoINTECO);
            cb.endMarkedContentSequence();
        }
    }

    private void createFooter(PdfContentByte cb, PdfWriter w, BaseFont texto, Document d) throws DocumentException {
        PropertiesManager pmgr = new PropertiesManager();

        if (w.getPageNumber() == 1) {
            ExportPageEventsUtils.addText(cb, Float.parseFloat(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.dateX")), Float.parseFloat(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.dateY")),
                    date, Color.WHITE, texto, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.date.size")));

            Image observatorio = ExportPageEventsUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.titlePage.multilanguage"), 600, 200, "Imagen logo Observatorio");
            if (observatorio != null) {
                observatorio.setAbsolutePosition(0, 180);
                cb.addImage(observatorio);
                cb.endMarkedContentSequence();
            }

            d.add(ExportPageEventsUtils.createRectangle(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recWidthFooterFirstPage")), Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recHeightFooterFirstPage")), Constants.VERDE_O_MP));

        } else {
            ExportPageEventsUtils.addFooter(d, w, cb, texto, footText, Constants.VERDE_O_MP);
        }

    }
}