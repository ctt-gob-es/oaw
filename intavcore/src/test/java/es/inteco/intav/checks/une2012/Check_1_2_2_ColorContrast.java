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
public final class Check_1_2_2_ColorContrast {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2 = "minhap.observatory.2.0.subgroup.1.2.2";

    private static final int CSS_COLOR_CONTRAST = 448;

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
    public void evaluateCSSSameColors() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSSameColorsName() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: white; background-color: white;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSMaximumContrast() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: #000; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateCSSMaximumContrastColorName() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: black; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateCSSNoColor() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: black; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }


    /**
     * *************
     * ABB     *
     * **************
     */

        /*
            MET 4.9.1 - Se verifica que las combinaciones de color de primer plano y de color de fondo en una misma regla de las hojas de estilo tienen el contraste suficiente (NUEVA)

            CSS_COLOR_CONTRAST= 448;

        */
    @Test
    public void MET_4_9_1_evaluateContrast() throws Exception {

        /* MET 4.9.1
            Title:      Se verifica que las combinaciones de color de primer plano y de color de fondo en una misma regla de las hojas de estilo tienen el contraste suficiente
            Subject:    estilos (<style>, "style", <link rel="stylesheet">)
            Check:      Combinaciones de primer plano (color) y de fondo (background-color, background) en una misma regla tienen contraste suficiente.
                            3:1 o 4.5:1 según el tamaño del texto si éste es conocido
                            3:1 si no se conoce el tamaño del texto
         */

        // Contraste 3.5:1, tamaño texto indefinido
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #888;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);


        // FALLA: Expected 1, Actual 0
        // Contraste 3.5:1, tamaño texto pequeño
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #999; font-size: 10pt}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);


        // FALLA: Expected 1, Actual 0
        // Contraste 3.5:1, tamaño texto pequeño (14pt)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #999; font-size: 14pt;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);


        // Contraste 3.5:1, tamaño texto grande (18pt)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #888; font-size: 18pt}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);


        // Contraste 3.5:1, tamaño texto grande (14pt+bold)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #888; font-size: 14pt; font-weight: bold}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);


        // Background property

        // Contraste 21:1, tamaño texto indefinido
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #000 url(\"img_tree.png\") no-repeat right top;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);

        // FALLA: Expected 1, Actual 0
        // Contraste 1:1, tamaño texto indefinido
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #FFF url(\"img_tree.png\") no-repeat right top;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #000 url(\"img_tree.png\") no-repeat right top;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);

        // Contraste 3.5:1, tamaño texto indefinido
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #888 url(\"img_tree.png\") no-repeat right top;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);

        // FALLA: Expected 1, Actual 0
        // Contraste 3.5:1, tamaño texto pequeño
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #888 url(\"img_tree.png\") no-repeat right top; font-size: 10pt}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);

        // FALLA: Expected 1, Actual 0
        // Contraste 3.5:1, tamaño texto pequeño (14pt)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #888 url(\"img_tree.png\") no-repeat right top; font-size: 14pt;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_RED_ZERO);

        // Contraste 3.5:1, tamaño texto grande (18pt)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #888 url(\"img_tree.png\") no-repeat right top; font-size: 18pt}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);

        // Contraste 3.5:1, tamaño texto grande (14pt+bold)
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background: #888 url(\"img_tree.png\") no-repeat right top; font-size: 14pt; font-weight: bold}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

}