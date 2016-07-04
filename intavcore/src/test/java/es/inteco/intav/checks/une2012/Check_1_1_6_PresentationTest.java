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
public final class Check_1_1_6_PresentationTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6 = "minhap.observatory.2_0.subgroup.1.1.6";

    private static final int DATA_ELEMENTS_ON_LAYOUT_TABLES = 45;
    private static final int PRESENTATION_ELEMENTS = 345;
    // Comprobar que no se genera contenido desde CSS mediante :before o :after
    private static final int CSS_GENERATED_CONTENT = 447;

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
    public void evaluateDataTable() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DATA_ELEMENTS_ON_LAYOUT_TABLES));
        // Si no hay tablas de maquetación ni etiquetas de presentación entonces pasa (no se considera no puntua)
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLayoutTable() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><td>Cell 1</td></tr><td>Cell 2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DATA_ELEMENTS_ON_LAYOUT_TABLES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateTableHeader() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><th>Header 1</th></tr><tr><td>Cell 1</td></tr><td>Cell 2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DATA_ELEMENTS_ON_LAYOUT_TABLES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateTableCaption() throws Exception {
        checkAccessibility.setContent("<html><body><table><caption>Lorem ipsum</caption><tr><td>Cell 0</td></tr><tr><td>Cell 1</td></tr><td>Cell 2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DATA_ELEMENTS_ON_LAYOUT_TABLES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateTableRolePresentation() throws Exception {
        checkAccessibility.setContent("<html><body><table role=\"presentation\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DATA_ELEMENTS_ON_LAYOUT_TABLES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluatePresentationTags() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), PRESENTATION_ELEMENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><head><title>Lorem</title></head><body><p>Lorem <font>ipsum</font></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), PRESENTATION_ELEMENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><head><title>Lorem</title></head><body><center><p>Lorem ipsum</p></center></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), PRESENTATION_ELEMENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSGeneratedContent() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main:before { content: \"> \";}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));

        checkAccessibility.setContent("<html><head><style>.main:before { content: \"Lorem: \";}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));

        checkAccessibility.setContent("<html><head><style>.main:after { content: \"Ipsum \";}</style><title>Lorem</title></head><body><p>Lorem</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));

        checkAccessibility.setContent("<html><head><style>.main:after { content: open-quote;}</style><title>Lorem</title></head><body><p>Lorem</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));

        checkAccessibility.setContent("<html><head><style>.main:after { content: open-quote;}</style><title>Lorem</title></head><body><p>Lorem</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));

        checkAccessibility.setContent("<html><head><style>.main:after { content: url(/img/image.png);}</style><title>Lorem</title></head><body><p>Lorem</p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_GENERATED_CONTENT));
    }


    /****************
     *      ABB     *
     ****************/

        /*
            MET 4.6.1 - Comprobacion de que es una tabla de maquetacion (ID 81 - modificado límite 150 caracteres)
            MET 4.6.2 - Se verifica que no haya tablas de maquetación que empleen elementos o atributos propios de tablas de datos (ID 88 - modificado el conjunto de elementos o atributos)
            MET 4.6.3 - Se verifica que no se se empleen elementos desaconsejados con carácter presentacional (NUEVA)
            MET 4.6.4 - Se verifica que no se incluya contenido que transmita información desde las hojas de estilo con los pseudoelementos :before o :after (NUEVA)

            DATA_ELEMENTS_ON_LAYOUT_TABLES = 45;
            PRESENTATION_ELEMENTS =345;
            CSS_GENERATED_CONTENT= 447;

        */
}