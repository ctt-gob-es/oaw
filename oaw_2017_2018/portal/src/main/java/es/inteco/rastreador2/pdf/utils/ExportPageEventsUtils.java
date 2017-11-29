/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;

import java.awt.*;


public final class ExportPageEventsUtils {

    private ExportPageEventsUtils() {
    }

    public static Rectangle createRectangle(int x, int y, Color color) {
        Rectangle rec = new Rectangle(x, y);
        rec.setBackgroundColor(color);
        return rec;
    }

    public static Image createImage(final String pathLogo, int scaleX, int scaleY, final String alt) {
        try {
            final Image logo = Image.getInstance(pathLogo);
            logo.scaleAbsolute(scaleX, scaleY);
            logo.setAlt(alt);
            return logo;
        } catch (Exception e) {
            return null;
        }
    }

    public static void addText(final PdfContentByte cb, float posX, float posY, String text, Color colorText, BaseFont font, int size) {
        cb.beginText();
        cb.setFontAndSize(font, size);
        cb.setColorFill(colorText);
        cb.moveText(posX, posY);
        cb.showText(text);
        cb.endText();
        cb.endMarkedContentSequence();
        cb.endMarkedContentSequence();
    }

    public static Rectangle addFooterLine(final Color color) {
        PropertiesManager pmgr = new PropertiesManager();
        Rectangle line = new Rectangle(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recWidthFooter")), Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recHeightFooter")));
        line.setTop(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterTop")));
        line.setBottom(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterBottom")));
        line.setLeft(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterLeft")));
        line.setRight(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterRight")));
        line.setBackgroundColor(color);
        return line;
    }

    public static void addFooter(Document d, PdfWriter w, PdfContentByte cb, BaseFont texto, String footText, Color color) throws DocumentException {
        PropertiesManager pmgr = new PropertiesManager();
        d.add(addFooterLine(color));
        ExportPageEventsUtils.addText(cb, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterX")), Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterY")),
                footText, color, texto, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.pageFooter")));
        String npage = String.valueOf(w.getCurrentPageNumber() - 1);
        ExportPageEventsUtils.addText(cb, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterRight")) - (npage.length() * 5), Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterY")),
                npage, color, texto, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.pageFooter")));
    }

}