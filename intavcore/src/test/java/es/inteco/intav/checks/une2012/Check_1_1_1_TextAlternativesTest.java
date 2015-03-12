package es.inteco.intav.checks.une2012;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_1_1_1_TextAlternativesTest extends EvaluateCheck {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1 = "minhap.observatory.2.0.subgroup.1.1.1";

    private static final int IMG_ALT_ID = 1;
    private static final int IMG_SUSPICIOUS_ALT = 100;
    private static final int IMG_LONGDESC_ID = 278;
    private static final int IMG_DECORATIVE_NO_TITLE_ID = 413;
    private static final int IMG_DIMENSIONS_DECORATIVE = 426;
    private static final int APPLET_ALTERNATIVES_ID = 414;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateAllAltTitleRoleCombinations() throws Exception {
        checkAccessibility.setContent("<img src=\"\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" title=\"Foo\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" title=\"Foo\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" title=\"Foo\" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" alt=\"\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\" \" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"Foo\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" title=\"Foo\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" title=\"Foo\" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\" title=\"Foo\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\" title=\"Foo\" role=\"link\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<img src=\"\" alt=\"Foo\" title=\"Foo\" role=\"presentation\">");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateNoImg() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p></body></html>");
        TestUtils.checkVerificacion(checkAccessibility, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_1, TestUtils.OBS_VALUE_NOT_SCORE);
    }

    @Test
    public void evaluateImgWithoutAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\">, <img src=\"\">");
        Assert.assertEquals(2, getNumProblems(checkAccessibility, IMG_ALT_ID));
    }

    @Test
    public void evaluateImgAltWithoutValue() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_ALT_ID));
    }

    @Test
    public void evaluateImgWithAltBlank() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_ALT_ID));
    }

    @Test
    public void evaluateImgWithAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"Lorem ipsum\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_ALT_ID));
    }

    @Test
    public void evaluateDecorativeNoAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\">, <img src=\"\">");
        // Las imágenes sin alternativa no se comprueban
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankNoTitle() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\">, <img src=\"\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankTitleBlank() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankAndTitle() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"Foo\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankAndTitleWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"   \">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateTitleNoAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\" title=\"Foo\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltAndTitle() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"Lorem\" title=\"Foo\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltAndTitleAndRoleLink() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"Lorem\" title=\"Foo\" role=\"link\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankAndTitleAndRoleLink() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"Foo\" role=\"link\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));

        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"\" role=\"link\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateAltBlankAndTitleAndRolePresentation() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"\" title=\"Foo\" role=\"presentation\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DECORATIVE_NO_TITLE_ID));
    }

    @Test
    public void evaluateNoLongdesc() throws Exception {
        checkAccessibility.setContent("<img src=\"\">, <img src=\"\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescBlank() throws Exception {
        checkAccessibility.setContent("<img src=\"\" longdesc=\"\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescAnchor() throws Exception {
        checkAccessibility.setContent("<img src=\"\" longdesc=\"#foo\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescRelativeURL() throws Exception {
        // Longdesc comprueba que sea una url válida (se pueda conectar) así que necesitamos una url válida para hacer de base
        checkAccessibility.setUrl("http://www.fundacionctic.org");

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"#foo\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por ../", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"../path/index.html#foo\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por ../", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"/actividad/ambitos-tecnologicos#foo\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por /", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));

        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" longdesc=\"actividad/ambitos-tecnologicos#bar\"></p></body></html>");
        Assert.assertEquals("URL relativa que empieza por letra", 0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescDescription() throws Exception {
        checkAccessibility.setContent("<img src=\"\" longdesc=\"Esto es una descripcion de una imagen.\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateLongdescAbsoluteURL() throws Exception {
        checkAccessibility.setContent("<img src=\"\" longdesc=\"http://www.google.com#foo\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_LONGDESC_ID));
    }

    @Test
    public void evaluateSuspiciousImgExtensionsAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"lorem.gif\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"lorem.png\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"lorem_gif\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"lorem.gifo\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));
    }

    @Test
    public void evaluateSuspiciousWordsAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"Foto\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"Fotomatón\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"foToGrafIa\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"Fotografía del acto\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));
    }

    @Test
    public void evaluateSuspiciousPatternsAlt() throws Exception {
        checkAccessibility.setContent("<img src=\"\" alt=\"pic1\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"pic001\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"img_1\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"Imagen desde blahblah\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"0001\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));

        checkAccessibility.setContent("<img src=\"\" alt=\"Lorem ipsum 0001\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_SUSPICIOUS_ALT));
    }

    @Test
    public void evaluateImgDimensions() throws Exception {
        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\"\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\" \">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\"\" role=\"presentation\">");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\"Foo\" role=\"presentation\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\"Foo\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" height=\"2\" alt=\"Foo\" role=\"link\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" width=\"2\" alt=\"Foo\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));

        checkAccessibility.setContent("<img src=\"\" height=\"2\" alt=\"Foo\">");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, IMG_DIMENSIONS_DECORATIVE));
    }

    @Test
    public void evaluateAppletNoAlternatives() throws Exception {
        checkAccessibility.setContent("<applet><param /><param />\r\n</applet>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAppletOnlyAltAttribute() throws Exception {
        checkAccessibility.setContent("<applet alt=\"Lorem ipsum\"></applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAppletOnlyTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet>Lorem ipsum</applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAppletAltAndTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet alt=\"Sic semper\">Lorem ipsum</applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAppletAltBlankNoTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet alt=\"\"></applet>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

}
