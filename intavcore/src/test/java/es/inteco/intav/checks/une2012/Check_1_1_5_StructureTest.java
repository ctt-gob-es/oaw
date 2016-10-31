package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_1_1_5_StructureTest extends EvaluateCheck {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5 = "minhap.observatory.2_0.subgroup.1.1.5";
    private static final int MORE_TEN_BRS = 436;
    private static final int DIV_MORE_THAN_150_CHARS = 33;
    private static final int BRS_SIMULATE_P = 16;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateZeroBRs() throws Exception {
        checkAccessibility.setContent("<html><p>Lorem ipsum<br/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateFourBRs() throws Exception {
        checkAccessibility.setContent("<html><p>Lorem<br/>ipsum<br/>Lorem<br/>Ipsum<br/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateTenBRs() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateMoreTenBRs() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(11, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateDiv() throws Exception {
        checkAccessibility.setContent("<html><div>" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum." +
                "</div></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DIV_MORE_THAN_150_CHARS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><div>" +
                "<!-- Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum. -->" +
                "Foo blah" +
                "</div></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DIV_MORE_THAN_150_CHARS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><div><script>" +
                "<![CDATA[Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum.]]>" +
                "</script>" +
                "Foo blah" +
                "</div></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DIV_MORE_THAN_150_CHARS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluatePBRs() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum." +
                "</p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BRS_SIMULATE_P));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><br /><br />" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum." +
                "</p></html>");
         evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BRS_SIMULATE_P));
         oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. <br/><br/>Nullam elit est, vulputate quis iaculis a, semper non quam. Pellentesque vulputate vestibulum dui sit amet volutpat. Phasellus at felis varius, vestibulum augue in, rutrum ipsum. Quisque ut massa hendrerit, commodo sem eu, pharetra turpis. Curabitur id sapien pulvinar, lobortis nibh vitae, vulputate erat. Nulla a felis facilisis, dictum metus sed, ullamcorper ligula. Sed odio ex, efficitur eget tempor vel, hendrerit ac tortor. Etiam iaculis sodales nulla. Nulla at augue euismod, ornare est sit amet, elementum quam. Nam id ornare lacus. Aliquam odio purus, dapibus quis vulputate ut, imperdiet posuere ligula. Donec ultrices faucibus mattis. Suspendisse vel neque et erat sollicitudin eleifend vitae eget massa. Nunc pellentesque lectus ut elit convallis, id interdum tellus bibendum." +
                "</p></html>");
         evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BRS_SIMULATE_P));
         oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluate() throws Exception {
        checkAccessibility.setContent("<html><head><title>Foo</title></head><body>\n" +
                "<div>\n" +
                "<div class=\"nivel_1\">\n" +
                "\t\n" +
                "\t    <!-- PAGINA: 1.3 Contenido: Parte superior -->\n" +
                "\t    <div class=\"seccion_inicio\">\n" +
                "\t        <!-- PAGINA: 1.3.1 Contenido: Parte superior: Imagen -->\n" +
                "        \t<div class=\"foto_primernivel_detalle\">\n" +
                "            \t <div class=\"foto_izq\">\n" +
                "               \t\t<div id=\"ctl00_PlaceHolderMain_ldwImagen\">\n" +
                "\t\n" +
                "\t\t\t\t\t    <img src=\"/es-ES/Prensa/En%20Portada/2013/PublishingImages/44.jpg\" alt=\"Banner Ministerio de Hacienda y Administraciones Públicas\" />\n" +
                "\t\t\t\t    \n" +
                "</div>\n" +
                "            \t</div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <!-- PAGINA: 1.3.2 Contenido: Parte superior: Descripcion de imagen -->\n" +
                "            <div id=\"descfoto\">\n" +
                "\t\t\t\t<div id=\"ctl00_PlaceHolderMain_ldwTextoDescriptivo\">\n" +
                "\t\n" +
                "\t\t\t\t\tEn esta página podrá consultar, en versiones en castellano e inglés, cifras actualizadas de los objetivos económicos y presupuestarios del Gobierno de España. \n" +
                "\t\t\t\t\n" +
                "</div>\n" +
                "\t\t\t</div>\n" +
                "        </div>\n" +
                "\t\n" +
                "\t    <div class=\"clear\"></div> \n" +
                "\t    \n" +
                "\t    <!-- PAGINA: 1.4 Contenido: Parte Html central -->\n" +
                "\t    <div class=\"seccion_texto\">\n" +
                "\t        <div id=\"ctl00_PlaceHolderMain_ldwTexto\">\n" +
                "\t\n" +
                "\t\t\t\t<ul><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Varios/PE%202015-18%2030_04_2015.pdf\" target=\"_blank\">Programa de Estabilidad<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Varios/SP%20PNR%20%202015%20FINAL.PDF\" target=\"_blank\">Programa Nacional de Reformas<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Presentaciones/PE%20ES.pdf\" target=\"_blank\">Presentación: Actualización Programa Estabilidad 2015-2018<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Varios/PE%20EN.pdf\" target=\"_blank\">Presentación: Actualización Programa Estabilidad 2015-2018 (Versión Inglés)<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Varios/ESTRATEGIA%2030%20ABRIL.pdf\" target=\"_blank\">Presentación &quot;Estrategia de Política Fiscal&quot;<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li><li><a title=\"Abre nueva ventana\" href=\"/Documentacion/Publico/GabineteMinistro/Notas%20Prensa/2015/CONSEJO%20DE%20MINISTROS/30-04-15%20NP%20PE%202015-2018.pdf\" target=\"_blank\">Nota de Prensa<img class=\"Technositepopup\" alt=\"Abre nueva ventana\" src=\"/_layouts/3082/images/img/popup.gif\"/></a></li></ul>\n" +
                "\t\t\t\n" +
                "</div>\n" +
                "\t    </div>\n" +
                "\t    \n" +
                "\t    <div class=\"clear\"></div> \n" +
                "\t    \n" +
                "\t    <!-- PAGINA: 1.5 Contenido: Parte inferior enlaces -->\n" +
                "\t    <div class=\"seccion_enlaces\">\n" +
                "\t    \n" +
                "\t        <!-- PAGINA: 1.5.1 Contenido: Parte inferior enlaces: Titulo enlaces -->\n" +
                "\t        <p class=\"titenlaces\">\n" +
                "\t            \n" +
                "\t        </p>\n" +
                "\t        \n" +
                "\t        <!-- PAGINA: 1.5.2 Contenido: Parte inferior enlaces: Lista enlaces -->\n" +
                "\t        <div id=\"ctl00_PlaceHolderMain_ldwEnlaces\">\n" +
                "\t\n" +
                "\t\t\t\t<div id=\"ctl00_PlaceHolderMain_enlacesbg__ControlWrapper_SummaryLinkFieldControl\" style=\"display:inline\"></div>\n" +
                "\t\t\t\n" +
                "</div>\n" +
                "\t    </div>\n" +
                "\t</div>\n" +
                "\n" +
                "\n" +
                "        </div>\n" +
                "</body></html> ");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DIV_MORE_THAN_150_CHARS));

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }
}