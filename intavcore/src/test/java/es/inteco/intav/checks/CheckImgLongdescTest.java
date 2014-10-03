package es.inteco.intav.checks;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CheckImgLongdescTest extends EvaluateCheck {

    private static final int IMG_LONGDESC_ID = 278;

    @Test
    public void evaluateNoLongdesc() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\">, <img src=\"\"></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescBlank() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"\"></p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescAnchor() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"#foo\"></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescRelativeURL() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"../path/index.html#foo\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por ../", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"/path/index.html#foo\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por /", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"path/index.html#bar\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por letra", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"another path/index.html#bar\"></p></body></html>");
        Assert.assertEquals("URL relativa que contiene espacios", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescDescription() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"Esto es una descripcion de una imagen.\"></p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescAbsoluteURL() throws Exception {
        final CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"http://www.google.com#foo\"></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

}
