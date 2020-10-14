package es.inteco.rastreador2.pdf.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.itextpdf.text.pdf.events.IndexEvents;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;

/**
 * Created by mikunis on 1/16/17.
 */
public class PDFTOCManagerTest {
	@Test
	public void addSection() throws Exception {
		PdfTocManager pdfTocManager = new PdfTocManager(new IndexEvents());
		assertEquals(1, pdfTocManager.addSection());
		assertEquals(2, pdfTocManager.getNumSection());
	}
}