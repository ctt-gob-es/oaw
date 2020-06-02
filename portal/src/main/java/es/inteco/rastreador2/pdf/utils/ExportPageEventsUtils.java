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

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;

/**
 * The Class ExportPageEventsUtils.
 */
public final class ExportPageEventsUtils {
	/**
	 * Instantiates a new export page events utils.
	 */
	private ExportPageEventsUtils() {
	}

	/**
	 * Creates the rectangle.
	 *
	 * @param x     the x
	 * @param y     the y
	 * @param color the color
	 * @return the rectangle
	 */
	public static Rectangle createRectangle(int x, int y, Color color) {
		Rectangle rec = new Rectangle(x, y);
		// PENDING
		// rec.setBackgroundColor(color);
		rec.setBackgroundColor(new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
		return rec;
	}

	/**
	 * Creates the image.
	 *
	 * @param pathLogo the path logo
	 * @param scaleX   the scale X
	 * @param scaleY   the scale Y
	 * @param alt      the alt
	 * @return the image
	 */
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

	/**
	 * Adds the text.
	 *
	 * @param cb        the cb
	 * @param posX      the pos X
	 * @param posY      the pos Y
	 * @param text      the text
	 * @param colorText the color text
	 * @param font      the font
	 * @param size      the size
	 */
	public static void addText(final PdfContentByte cb, float posX, float posY, String text, Color colorText, BaseFont font, int size) {
		cb.beginMarkedContentSequence(PdfName.TEXT);
		cb.beginText();
		cb.setFontAndSize(font, size);
		// cb.setColorFill(colorText);
		// PENDING
		cb.setColorFill(new BaseColor(colorText.getRed(), colorText.getGreen(), colorText.getBlue()));
		cb.moveText(posX, posY);
		cb.showText(text);
		cb.endText();
		cb.endMarkedContentSequence();
	}

	/**
	 * Adds the footer line.
	 *
	 * @param color the color
	 * @return the rectangle
	 */
	public static Rectangle addFooterLine(final Color color) {
		PropertiesManager pmgr = new PropertiesManager();
		Rectangle line = new Rectangle(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recWidthFooter")),
				Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.recHeightFooter")));
		line.setTop(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterTop")));
		line.setBottom(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterBottom")));
		line.setLeft(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterLeft")));
		line.setRight(Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterRight")));
		// PENDING
		// line.setBackgroundColor(color);
		line.setBackgroundColor(new BaseColor(color.getRed(), color.getGreen(), color.getBlue()));
		return line;
	}

	/**
	 * Adds the footer.
	 *
	 * @param d        the d
	 * @param w        the w
	 * @param cb       the cb
	 * @param texto    the texto
	 * @param footText the foot text
	 * @param color    the color
	 * @throws DocumentException the document exception
	 */
	public static void addFooter(Document d, PdfWriter w, PdfContentByte cb, BaseFont texto, String footText, Color color) throws DocumentException {
		PropertiesManager pmgr = new PropertiesManager();
		d.add(addFooterLine(color));
		ExportPageEventsUtils.addText(cb, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterX")), Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterY")),
				footText, color, texto, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.pageFooter")));
		String npage = String.valueOf(w.getCurrentPageNumber() - 1);
		ExportPageEventsUtils.addText(cb, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.lineFooterRight")) - (npage.length() * 5),
				Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.textFooterY")), npage, color, texto, Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.pageFooter")));
	}
}