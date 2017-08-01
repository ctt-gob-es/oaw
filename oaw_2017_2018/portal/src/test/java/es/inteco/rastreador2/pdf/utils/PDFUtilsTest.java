package es.inteco.rastreador2.pdf.utils;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
public class PDFUtilsTest {

    @Test
    public void testFormatSeedName() {
        Assert.assertNull(PDFUtils.formatSeedName(null));
        Assert.assertEquals("", PDFUtils.formatSeedName(""));
        Assert.assertEquals("prueba", PDFUtils.formatSeedName("PrUeBa"));
        Assert.assertEquals("prueba", PDFUtils.formatSeedName("PrUeBa  "));
        Assert.assertEquals("prueba", PDFUtils.formatSeedName(" PrUeBa  "));
        Assert.assertEquals("prueba_espacio", PDFUtils.formatSeedName("PrUeBa esPaCio"));
        Assert.assertEquals("prueba_espacio", PDFUtils.formatSeedName("PrUeBa, esPaCio"));
        Assert.assertEquals("prueba_espacio_", PDFUtils.formatSeedName("PrUeBa, esPaCio."));
        Assert.assertEquals("prueba-guion", PDFUtils.formatSeedName("PrUeBa-Guión"));
        Assert.assertEquals("prueba-guion", PDFUtils.formatSeedName("PRUEBA-GUIÓN"));
        Assert.assertEquals("prueba_barra", PDFUtils.formatSeedName("Prueba/Barra"));
        Assert.assertEquals("prueba_barra", PDFUtils.formatSeedName("Prueba\\Barra"));
    }

}
