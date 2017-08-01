package es.inteco.rastreador2.pdf.template;


import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import java.awt.*;

/**
 * Build the header and footer of the document.
 *
 * @author j.delamo
 * @version 1.0
 */
public class ExportPageEvents extends PdfPageEventHelper {
    /**
     * Path of the font arial.
     */
    private String pathArial;
    /**
     * Size font of the footer.
     */
    private int pageFooter;
    /**
     * First value of the RGB color.
     */
    private int rRedColor;
    /**
     * Second value of the RGB color.
     */
    private int gRedColor;
    /**
     * Third value of the RGB color.
     */
    private int bRedColor;
    /**
     * Width of the footer in the first page.
     */
    private int recWidthFooterFirstPage;
    /**
     * Height of the foot in the first page.
     */
    private int recHeightFooterFirstPage;
    /**
     * Width of the footer of every page.
     */
    private int recWidthFooter;
    /**
     * Height of the footer of every page.
     */
    private int recHeightFooter;
    /**
     * Set footer line top.
     */
    private int lineFooterTop;
    /**
     * Set footer line bottom.
     */
    private int lineFooterBottom;
    /**
     * Set footer line left.
     */
    private int lineFooterLeft;
    /**
     * Set footer line right.
     */
    private int lineFooterRight;
    /**
     * First color RGB for the line footer background.
     */
    private int lineBackgrR;
    /**
     * Second color RGB for the line footer background.
     */
    private int lineBackgrG;
    /**
     * Third color RGB for the line footer background.
     */
    private int lineBackgrB;
    /**
     * First color RGB for filling paths.
     */
    private int contentByteColorFillR;
    /**
     * Second color RGB for filling paths.
     */
    private int contentByteColorFillG;
    /**
     * Third color RGB for filling paths.
     */
    private int contentByteColorFillB;
    /**
     * Start of the text line in the X position of the document footer page.
     */
    private int textFooterX;
    /**
     * Start of the text line in the Y position of the document footer page.
     */
    private int textFooterY;
    /**
     * Text number page in the X position of the document footer page.
     */
    private int textPageFooterX;
    /**
     * Text number page in the Y position of the document footer page.
     */
    private int textPageFooterY;
    /**
     * Set the logo path of the header.*
     */
    private String pathLogo;
    /**
     * Constant for X absolute position of the header image.
     */
    private static final int POS_X = 72;
    /**
     * Constant for Y absolute position of header image.
     */
    private static final int POS_Y = 750;
    /**
     * Constant for the X resolution for the header image.
     */
    private static final float SCALE_LOGO_X = 100;
    /**
     * Constant for the X resolution for the header image.
     */
    private static final float SCALE_LOGO_Y = 65;

    private float dateX;
    private float dateY;
    private int dateSize;

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

    public ExportPageEvents(String footText, String date) {
        this.footText = footText;
        this.date = date;
    }

    /**
     * This is the event for headers and footers.
     *
     * @param w A PdfWriter object.
     * @param d A Document object.
     */
    public final void onEndPage(final PdfWriter w, final Document d) {
        try {
            getDynamicPropertiesHeadFoot();
            PdfContentByte cb = w.getDirectContent();

            //Font of the footer.
            BaseFont texto = BaseFont.createFont(pathArial, BaseFont.CP1252, BaseFont.EMBEDDED);

            //Image of header.
            Image logointeco;
            logointeco = Image.getInstance(pathLogo);
            logointeco.scaleAbsolute(SCALE_LOGO_X, SCALE_LOGO_Y);
            logointeco.setAbsolutePosition(POS_X, POS_Y);
            logointeco.setAlt("Informe de Accesibilidad. ");
            cb.addImage(logointeco);
            cb.endMarkedContentSequence();
            cb.endMarkedContentSequence();

            //Footer
            if (!lastPage) {
                if (w.getPageNumber() == 1) {
                    cb.beginText();
                    cb.setFontAndSize(texto, dateSize);
                    cb.setColorFill(Color.WHITE);
                    cb.moveText(dateX, dateY);
                    cb.showText(date);
                    cb.endText();
                    cb.endMarkedContentSequence();
                    cb.endMarkedContentSequence();
                    //Rectangle of the footer.
                    Rectangle rec = new Rectangle(recWidthFooterFirstPage, recHeightFooterFirstPage);
                    rec.setBackgroundColor(new Color(rRedColor, gRedColor, bRedColor));
                    d.add(rec);
                } else {
                    Rectangle line = new Rectangle(recWidthFooter, recHeightFooter);
                    line.setTop(lineFooterTop);
                    line.setBottom(lineFooterBottom);
                    line.setLeft(lineFooterLeft);
                    line.setRight(lineFooterRight);
                    line.setBackgroundColor(new Color(lineBackgrR, lineBackgrG, lineBackgrB));
                    d.add(line);

                    //Page number.
                    String npage = String.valueOf(w.getCurrentPageNumber() - 1);
                    cb.beginText();
                    cb.setFontAndSize(texto, pageFooter);
                    cb.setRGBColorFill(contentByteColorFillR, contentByteColorFillG, contentByteColorFillB);
                    cb.moveText(textFooterX, textFooterY);
                    //Text of the footer
                    cb.showText(footText);
                    cb.moveText(textPageFooterX, textPageFooterY);
                    //Show the page number.

                    cb.showText(npage);
                    cb.endText();
                    cb.endMarkedContentSequence();
                    cb.endMarkedContentSequence();
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception", ExportPageEvents.class, es.inteco.common.logging.Logger.LOG_LEVEL_ERROR, e);
        }
    }

    /**
     * Get the dynamic properties.
     */
    public final void getDynamicPropertiesHeadFoot() {
        PropertiesManager properties = new PropertiesManager();
        final String PDF_PROPERTIES = "pdf.properties";
        //Create the variables with the values of the file of properties.
        pathArial = properties.getValue(PDF_PROPERTIES, "path.pdf.font");
        recWidthFooterFirstPage = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.recWidthFooterFirstPage"));
        recHeightFooterFirstPage = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.recHeightFooterFirstPage"));
        recWidthFooter = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.recWidthFooter"));
        recHeightFooter = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.recHeightFooter"));
        lineFooterTop = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineFooterTop"));
        lineFooterBottom = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineFooterBottom"));
        lineFooterLeft = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineFooterLeft"));
        lineFooterRight = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineFooterRight"));
        lineBackgrR = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineBackgrR"));
        lineBackgrG = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineBackgrG"));
        lineBackgrB = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.lineBackgrB"));
        rRedColor = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.rRedColor"));
        gRedColor = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.gRedColor"));
        bRedColor = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.bRedColor"));
        pageFooter = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.pageFooter"));
        contentByteColorFillR = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.contentByteColorFillR"));
        contentByteColorFillG = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.contentByteColorFillG"));
        contentByteColorFillB = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.contentByteColorFillB"));
        textFooterX = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.textFooterX"));
        textFooterY = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.textFooterY"));
        textPageFooterX = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.textPageFooterX"));
        textPageFooterY = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.textPageFooterY"));
        pathLogo = properties.getValue(PDF_PROPERTIES, "path.inteco.logo");
        dateX = Float.parseFloat(properties.getValue(PDF_PROPERTIES, "pdf.dateX"));
        dateY = Float.parseFloat(properties.getValue(PDF_PROPERTIES, "pdf.dateY"));
        dateSize = Integer.parseInt(properties.getValue(PDF_PROPERTIES, "pdf.date.size"));
    }
}