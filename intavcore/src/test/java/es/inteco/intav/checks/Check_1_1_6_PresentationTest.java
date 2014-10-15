package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.*;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_1_6_PresentationTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6 = "minhap.observatory.2.0.subgroup.1.1.6";

    private static final int DATA_ELEMENTS_ON_LAYOUT_TABLES = 45;
    private static final int PRESENTATION_ELEMENTS =345;
    // Comprobar que no se genera contenido desde CSS mediante :before o :after
    // private static final int CONTENT_FORM_CSS =;

    private CheckAccessibility checkAccessibility;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
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
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), PRESENTATION_ELEMENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

}