package es.ctic.css.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.ctic.css.CSSImportedResource;
import es.ctic.css.CSSResource;
import es.ctic.css.CSSStyleSheetResource;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ImportedCSSExtractorTest {

    private CheckAccessibility checkAccessibility;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void extract() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css');</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
        Assert.assertEquals("http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css", evaluation.getCssResources().get(1).getStringSource());
    }

    @Test
    public void testMediaPrint() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css') print; * {font-size: 100%}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, evaluation.getCssResources().size());
    }

    @Test
    public void testMediaScreen() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css') screen; * {font-size: 100%}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }

    @Test
    public void testMediaPrintAndScreen() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css') print, screen; * {font-size: 100%}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }

    @Test
    public void testMediaAll() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css') all; * {font-size: 100%}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }

    @Test
    public void testMediaQuery() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css') screen and (color); * {font-size: 100%}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }


    @Test
    public void evaluateCSSImportDirective() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://www.tawdis.net/system/modules/org.fundacionctic.taw/resources/css/rumo_style.css');</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }

    @Test
    public void evaluateLevel2ImportDirective() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://localhost/html/css/styles_a.css');</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(3, evaluation.getCssResources().size());
    }

    @Test
    public void evaluateLevel3ImportDirective() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://localhost/html/css/styles_a1.css');</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(4, evaluation.getCssResources().size());
    }


    @Test
    public void evaluateCiclycImportDirective() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://localhost/html/css/styles_a_a.css');</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, evaluation.getCssResources().size());
    }

    @Test
    public void evaluateImportColorContrastError() throws Exception {
        checkAccessibility.setContent("<html><head><style>@import url('http://localhost/html/css/styles_a.css');</style><title>Lorem</title></head><body><p class=\"error_contraste\">Lorem <u>ipsum</u></p><ul><li>Foo</li><li>bar</li></ul></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(3, evaluation.getCssResources().size());
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), 448));
    }

    @Test
    public void testPortal() throws Exception {
        ImportedCSSExtractor cssExtractor = new ImportedCSSExtractor();
        CSSResource cir = new CSSImportedResource("http://www.mjusticia.gob.es/estatico/cs/portal/css/", "http://www.mjusticia.gob.es/estatico/cs/portal/css/estilos.css?v=2");
        List<CSSResource> cssResources = cssExtractor.extract(cir);
//        Assert.assertEquals(1, cssResources.size());
    }

}