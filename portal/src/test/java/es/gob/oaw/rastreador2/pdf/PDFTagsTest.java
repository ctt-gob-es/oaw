package es.gob.oaw.rastreador2.pdf;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class PDFTagsTest.
 */
public class PDFTagsTest {
	/** The Constant DEST. */
	public static final String DEST = "/home/alvaro/Downloads/test_tags.pdf";

	/**
	 * Manipulate pdf.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void manipulatePdf() throws Exception {
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(DEST)));
		writer.setPdfVersion(PdfWriter.VERSION_1_7);
		// TAGGED PDF
		// Make document tagged
		writer.setTagged();
		// ===============
		// PDF/UA
		// Set document metadata
		writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
		document.addLanguage("en-US");
		document.addTitle("English pangram");
		writer.createXmpMetadata();
		// =====================
		document.open();
		Paragraph p = new Paragraph();
		// PDF/UA
		// Embed font
		Font font = FontFactory.getFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED, 20);
		p.setFont(font);
//		// ==================
//		Chunk c = new Chunk("The quick brown ");
//		p.add(c);
//		Image i = Image.getInstance(FOX);
//		c = new Chunk(i, 0, -24);
//		// PDF/UA
//		// Set alt text
//		c.setAccessibleAttribute(PdfName.ALT, new PdfString("Fox"));
//		// ==============
//		p.add(c);
//		p.add(new Chunk(" jumps over the lazy "));
//		i = Image.getInstance(DOG);
//		c = new Chunk(i, 0, -24);
//		// PDF/UA
//		// Set alt text
//		c.setAccessibleAttribute(PdfName.ALT, new PdfString("Dog"));
//		// ==================
//		p.add(c);
		document.add(p);
		p = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n", font);
		document.add(p);
		List list = new List(true);
		list.add(new ListItem("quick", font));
		list.add(new ListItem("brown", font));
		list.add(new ListItem("fox", font));
		list.add(new ListItem("jumps", font));
		list.add(new ListItem("over", font));
		list.add(new ListItem("the", font));
		list.add(new ListItem("lazy", font));
		list.add(new ListItem("dog", font));
		document.add(list);
		document.close();
	}
}
