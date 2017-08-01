package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.pdf.events.IndexEvents;
import es.ctic.rastreador2.pdf.utils.PdfTocManager;
import org.junit.Test;

import static org.junit.Assert.*;

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