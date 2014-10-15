package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_2_1_7_CompatibilityTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7 = "minhap.observatory.2.0.subgroup.2.1.7";

    /* Documento tenga un DTD válido (TODO: incluir HTML5) */
    private static final int DOCTYPE = 323;
    /* Código HTML sea parseable (apertura y cierre de etiquetas y anidamiento correcto de elementos) */
    /* Se verifica que no se repite el mismo atributo con diferente valor en el mismo elemento */
    private static final int DUPLICATED_ATTRIBUTES = 441;
    /* Se verifica que los valores de los atributos están entrecomillados */
    private static final int QUOTED_ATTRIBUTES = 439;
    /* Se verifica que el valor de los atributos que deben tener un valor único por página (“id”, “accesskey”) efectivamente tienen un valor único */
    private static final int IDS_UNIQUES = 438;
    /* Se verifica que el código CSS es parseable (bien formado, sin errores de sintaxis) */
    
    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateNoDoctype() throws Exception {
        checkAccessibility.setContent("<html><body><p id=\"lorem\">Lorem ipsum</p><p id=\"lorem\">Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DOCTYPE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
         public void evaluateIDsUniques() throws Exception {
        checkAccessibility.setContent("<html><body><p id=\"lorem\">Lorem ipsum</p><p id=\"lorem\">Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), IDS_UNIQUES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateNoQuotedSlashCharAttribute() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum <img src=/images/blank.png></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), QUOTED_ATTRIBUTES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateNoQuotedWhiteSpaceAttribute() throws Exception {
        checkAccessibility.setContent("<html><body><p id=lorem ipsum>Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), QUOTED_ATTRIBUTES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateSingleAttribute() throws Exception {
        checkAccessibility.setContent("<html><body><p single>Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), QUOTED_ATTRIBUTES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateDuplicatedAttribute() throws Exception {
        checkAccessibility.setContent("<html><body><p class=\"single\" class=\"doble\">Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_ATTRIBUTES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateDuplicatedAttributeSameValue() throws Exception {
        checkAccessibility.setContent("<html><body><p class=\"single\" class=\"single\">Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_ATTRIBUTES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

/*    @Test
    public void evaluateCSS() throws Exception {
        checkAccessibility.setContent("");
        checkAccessibility.setUrl("http://www.fundacionctic.org");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
    }*/

}

