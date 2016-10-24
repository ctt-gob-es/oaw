package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_2_3_AccesibilitySection {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3 = "minhap.observatory.2_0.subgroup.1.2.3";

    private static final int HAS_NOT_SECTION_LINK = 126;
    private static final int ACCESSIBILITY_DECLARATION_NOT_CONTACT = 148;
    private static final int ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE = 149;
    private static final int ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL = 463;

    private CheckAccessibility checkAccessibility;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012-no-broken");
    }

    @Test
    public void testNoSectionLink() throws Exception {
        checkAccessibility.setContent("<html><head><title>Foo</title></head><body><p>Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void testAccesibilityPageNoContact() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Nivel de conformidad AA (doble A)</p><p>Fecha última revisión 01/01/2015</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void testAccesibilityPageNoDate() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Nivel de conformidad AA (doble A)</p><p><a>Contactar</a></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void testAccesibilityPageNoConformance() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void testAccesibilityConformanceFormats() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel de conformidad AA (doble A)</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Prioridad 2</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel doble-a</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel AA</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityContactLink() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p><a>Contacte con nosotros</a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p><a>contacte con nosotros. </a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p><a>buzón de contacto</a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityPageEnglishDate() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01-24-2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 1.24.2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityDate() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7 de Octubre de 2015</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7/9/2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7-9-2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7.9.2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07/09/2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07-09-2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07.09.2015</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityPageYearDate() throws Exception {
        checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 2015/12/31</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityPatternsPage() throws Exception {
        checkAccessibility.setContent("<html><head><title>Portal title - subtitle - Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 2015/12/31</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testURL() throws Exception {
        checkAccessibility.setUrl("http://www.agendadigital.gob.es");
        Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setUrl("http://www.thespanisheconomy.com/portal/site/tse/menuitem.a2f626f6152b3957b88f9b10026041a0/?vgnextoid=5080a57283d7f310VgnVCM1000002006140aRCRD");
        evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityPageNotLinked() throws Exception {
        checkAccessibility.setUrl("http://www.agendadigital.gob.es/Paginas/accesibilidad.aspx");
        Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testAccesibilityPage() throws Exception {
        checkAccessibility.setUrl("http://www.creatuempresa.org/es-ES/Paginas/accesibilidad.aspx");
        Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }
}