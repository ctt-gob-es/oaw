package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_2_1_3_FormsTest extends EvaluateCheck {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3 = "minhap.observatory.2_0.subgroup.2.1.3";

    /* Nota: Se considera etiqueta asociada a <label> (con texto) asociado explícitamente; “aria-labelledby” con un “id” correspondiente a un elemento con contenido textual; “aria-label” o “títle” con contenido. */
    /* Elementos input tienen una etiqueta explícitamente asociada */
    private static final int INPUT_LABEL = 57;
    /* Elementos label no asociados controla que exista for con un id válido (a un control) */
    private static final int LABEL_NO_FOR = 67;
    /* Elementos select tienen una etiqueta explícitamente asociada */
    private static final int SELECT_LABEL = 91;
    /* Elementos textarea tienen una etiqueta explícitamente asociada */
    private static final int TEXTAREA_LABEL = 95;
    /* Se verifica que todos los elementos “label” que están asociados explícitamente y son la única etiqueta asociada a un control (título, aria-label, etc.) no están ocultos con CSS. */
    private static final int LABEL_CSS_HIDDEN = 461;
    /* Tres o más de botones de radio y/o casillas de verificación agrupados (con el mismo “name”) se agrupan bajo su correspondiente fieldset> */
    private static final int GROUPED_SELECTION_BUTTONS = 443;
    /* Se verifica que no existan dos o más elementos de encabezado dentro de un elemento <form> (en lugar de usar “fieldset”) */
    private static final int HEADERS_AS_LEGEND = 429;
    private static final int HEADERS_AS_LEGEND_SHAREPOINT = 466;
    /* En formularios con X o más campos (text, file, password, select, textarea) debe existir al menos un fieldset y un legend (Heuristico X=10) */
    private static final int COMPLEX_FORMS = 430;
    /* Se verifica que todos los “fieldset” tengan un único elemento “legend” con contenido (primer elemento semántico hijo).*/
    private static final int LEGEND_FIRST_CHILD = 444;
    /* Elementos select con más de X opciones sin agrupar bajo elementos “optgroup”. (X=20) */
    private static final int OPTGROUP = 406;
    /* Elementos select con opciones que comiencen por sucesiones de 3 o más caracteres repetidos no alfanuméricos (P. ej: “___”, “***”, “......”, etc.).*/
    private static final int SIMULATED_OPTGROUP = 417;
    /* Elementos optgroup tienen un atributo “label” con contenido */
    private static final int OPTGROUP_LABEL = 407;
    /* Se identifican los campos obligatorios en formularios con más de N campos de texto (se busca los términos “obligatorio”, “opcional” o sinónimos equivalentes en el texto, alternativas o títulos presentes dentro del formulario <form>). (N = 4) */
    private static final int REQUIRED_CONTROLS = 446;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateComplexForm() throws Exception {
        checkAccessibility.setContent("<form><label for=\"control\">Etiqueta</label><input id=\"control\" /></form>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), COMPLEX_FORMS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<form><label for=\"control\">Etiqueta</label><fieldset><legend>Lorem</legend><input id=\"control\" /><input id=\"control\" /></fieldset><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), COMPLEX_FORMS));

        checkAccessibility.setContent("<form><label for=\"control\">Etiqueta</label><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), COMPLEX_FORMS));

        checkAccessibility.setContent("<form><label for=\"control\">Etiqueta</label><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /><input id=\"control\" /></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), COMPLEX_FORMS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_RED_ZERO);
    }


    @Test
    public void evaluateLabelFor() throws Exception {
        checkAccessibility.setContent("<form><label for=\"control\">Etiqueta</label><input id=\"control\" /></form>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), LABEL_NO_FOR));

        checkAccessibility.setContent("<label for=\"none\">Etiqueta</label><input id=\"control\" />");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), LABEL_NO_FOR));
    }


    @Test
    public void evaluateInputLabelAssociation() throws Exception {
        checkAccessibility.setContent("<label for=\"correo\">Correo</label><input id=\"correo\" type=\"email\"/>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<input title=\"Correo\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<input aria-label=\"Correo\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<span id=\"label_correo\">Correo</span><input aria-labelledby=\"label_correo\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<input type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<input aria-label=\"\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<input aria-label=\"&nbsp;\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<span>Correo</span><input aria-labelledby=\"label_correo\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), INPUT_LABEL));

        checkAccessibility.setContent("<span id=\"label_correo\"></span><input aria-labelledby=\"label_correo\" type=\"email\"/>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), INPUT_LABEL));
    }

    @Test
    public void evaluateSelectLabelAssociation() throws Exception {
        checkAccessibility.setContent("<label for=\"selector\">Selector</label><select id=\"selector\"><option>Opción 1</option></select>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<form><label for=\"_selector_\">Selector</label><select id=\"_selector_\" name=\"otra_cosa\"><option>Opción 1</option></select></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<span class=\"aui-field-content\">\n" +
                "\n" +
                "\t\t\t<label class=\"aui-field-label-inline-label\"\n" +
                "                   for=\"_101_INSTANCE_HQbMoL5bmHVo_ocerSearchContainerPageIterator_page\">\n" +
                "                Página\n" +
                "                <span class=\"aui-helper-hidden-accessible\">(Cambiar el valor de este campo provocará que se recargue la página.)</span>\n" +
                "            </label>\n" +
                "\n" +
                "\t\t<span class='aui-field-element '>\n" +
                "\t\t\t<select class=\"aui-field-input aui-field-input-select aui-field-input-menu\"\n" +
                "                    id=\"_101_INSTANCE_HQbMoL5bmHVo_ocerSearchContainerPageIterator_page\"\n" +
                "                    name=\"_101_INSTANCE_HQbMoL5bmHVo_page\" onchange=\"_101_INSTANCE_HQbMoL5bmHVo_curupdateCur(this);\">\n" +
                "                <option selected value=\"1\">1</option>\n" +
                "                <option value=\"2\">2</option>\n" +
                "                <option value=\"3\">3</option>\n" +
                "            </select>\n" +
                "\t\t</span>\n" +
                "\n" +
                "\t\t\t<span class=\"aui-suffix\">\n" +
                "\t\t\t\tde 8\n" +
                "\t\t\t</span>\n" +
                "</span>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<select title=\"Selector\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<select aria-label=\"Selector\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<span id=\"label_selector\">Selector</span><select aria-labelledby=\"label_selector\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<form><select id=\"selector\"><option>Opción 1</option></select></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<select aria-label=\"\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<select aria-label=\"&nbsp;\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<span>Selector</span><select aria-labelleby=\"label_selector\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SELECT_LABEL));

        checkAccessibility.setContent("<span id=\"label_selector\"></span><select aria-labelleby=\"label_selector\"><option>Opción 1</option></select>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SELECT_LABEL));
    }

    @Test
    public void evaluateTextAreaLabelAssociation() throws Exception {
        checkAccessibility.setContent("<label for=\"texto\">Texto</label><textarea id=\"texto\">Lorem ipsum</textarea>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<textarea title=\"Texto\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<textarea aria-label=\"Texto\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<span id=\"label_texto\">Texto</span><textarea aria-labelledby=\"label_texto\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<form><textarea id=\"texto\">Lorem ipsum</textarea></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<textarea aria-label=\"\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<textarea aria-label=\"&nbsp;\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<form><span>Texto</span><textarea id=\"label_texto\">Lorem ipsum</textarea></form>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));

        checkAccessibility.setContent("<span id=\"label_texto\"></span><textarea aria-labelledby=\"label_texto\">Lorem ipsum</textarea>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), TEXTAREA_LABEL));
    }

    @Test
    public void evaluateNoOptgroup() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "<option>Opción 4</option>" +
                "<option>Opción 5</option>" +
                "<option>Opción 6</option>" +
                "<option>Opción 7</option>" +
                "<option>Opción 8</option>" +
                "</select>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), OPTGROUP_LABEL));
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), SIMULATED_OPTGROUP));
    }

    @Test
    public void evaluateManyOptionsOptGroupLabel() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"Grupo 1\"/>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, OPTGROUP_LABEL));
    }


    @Test
    public void evaluateOptgroupNoLabel() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup/>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, OPTGROUP_LABEL));
    }

    @Test
    public void evaluateOptgroupLabelBlank() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"\"/>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, OPTGROUP_LABEL));
    }

    @Test
    public void evaluateOptgroupWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"    \"/>" +
                "<option>Opción 10</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, OPTGROUP_LABEL));
    }

    @Test
    public void evaluateOptgroupNbspSpaces() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"&nbsp;\"/>" +
                "<option>Opción 10</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, OPTGROUP_LABEL));
    }

    @Test
    public void evaluateFewOptions() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "<option>Opción 4</option>" +
                "<option>Opción 5</option>" +
                "<option>Opción 6</option>" +
                "<option>Opción 7</option>" +
                "<option>Opción 8</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, OPTGROUP));
    }

    @Test
    public void evaluateManyOptions() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Seleccione ----</option>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "<option>Opción 24</option>" +
                "</select>");
        // 25 opciones pasa
        Assert.assertEquals(0, getNumProblems(checkAccessibility, OPTGROUP));

        checkAccessibility.setContent("<select>" +
                "<option>Seleccione ----</option>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "<option>Opción 24</option>" +
                "<option>Opción 25</option>" +
                "</select>");
        // 26 opciones falla
        Assert.assertEquals(1, getNumProblems(checkAccessibility, OPTGROUP));
    }

    @Test
    public void evaluateManyOptionsGrouped() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"Grupo 1\"/>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, OPTGROUP));
    }

    @Test
    public void evaluate2NoOptgroup() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>* Opción 1</option>" +
                "<option>* Opción 2</option>" +
                "<option>* Opción 3</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP));
    }

    @Test
    public void evaluateOnly2Items() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>--- Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP));
    }

    @Test
    public void evaluateMixedChars() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>**- Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP));
    }

    @Test
    public void evaluateTwoGroups() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>***- Opción A</option>" +
                "<option>Opción 1</option>" +
                "<option>***- Opción B</option>" +
                "<option>Opción 2</option>" +
                "</select>");
        Assert.assertEquals(2, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP));
    }

    @Test
    public void evaluateGroupedSelectionButtonsTwoButtons() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<input name=\"grupo\" type=\"radio\"> Opción A" +
                "<input name=\"grupo\" type=\"radio\"> Opción B" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));

        checkAccessibility.setContent("<form>" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción A" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción B" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));
    }

    @Test
    public void evaluateGroupedSelectionButtonsThreeButtons() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<input name=\"grupo\" type=\"radio\"> Opción A" +
                "<input name=\"grupo\" type=\"radio\"> Opción B" +
                "<input name=\"grupo\" type=\"radio\"> Opción C" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));

        checkAccessibility.setContent("<form>" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción A" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción B" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción C" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));
    }

    @Test
    public void evaluateGroupedSelectionButtonsThreeButtonsGrouped() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<input name=\"grupo\" type=\"radio\"> Opción A" +
                "<input name=\"grupo\" type=\"radio\"> Opción B" +
                "<input name=\"grupo\" type=\"radio\"> Opción C" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));

        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción A" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción B" +
                "<input name=\"grupo\" type=\"checkbox\"> Opción C" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, GROUPED_SELECTION_BUTTONS));
    }

    @Test
    public void evaluateLegendFirstChild() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateLegendFirstChildDiv() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><div><legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</div></fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));

        checkAccessibility.setContent("<form>" +
                "<fieldset><div>Lorem ipsum</div><legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</fieldset></form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateLegendDoubleChildDiv() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><div>Lorem</div><div>Ipsum</div><legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</div></fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateH2FirstChild() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><h2>Foo</h2>" +
                "<legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateLegendFirstChildWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset>    <div>   <legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</div></fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));

        checkAccessibility.setContent("<form>" +
                "<fieldset>  " + System.lineSeparator() + "  <div> " + System.lineSeparator() + "  <legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</div></fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateLegendFirstChildText() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label for=\"foo\">Lorem</label><input id=\"foo\" />" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateLegendSpanText() throws Exception {
        checkAccessibility.setContent("\t\t\t<form action=\"busqueda.asp\" method=\"get\" name=\"busqueda\" id=\"formBuscadores\">\n" +
                "\t\t\t<div><input type=\"hidden\" name=\"idioma\" value=\"1\"></div>\n" +
                "\t\t\t<fieldset>\n" +
                "\t\t\t<legend><span class=\"legend_buscador\">Buscador</span></legend>\n" +
                "\t\t\t</fieldset>\n" +
                "\t\t\t</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LEGEND_FIRST_CHILD));
    }

    @Test
    public void evaluateNonRequiredControls() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateRequiredControls() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateRequiredControlsLabel() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label>Campo obligatorio</label><input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateRequiredControlsAbbr() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label>Lorem<abbr title=\"Campo obligatorio\">*</abbr></label><input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateRequiredControlsTitle() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label>Lorem*</label><input title=\"Campo obligatorio\">" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateRequiredControlsText() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<div>Los campos marcados con * son obligatorios" +
                "<fieldset><legend>Grupo</legend>" +
                "<label>Lorem*</label><input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));
    }

    @Test
    public void evaluateRequiredControlsImg() throws Exception {
        checkAccessibility.setContent("<form>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label>Lorem <img src=\"img/asterisk.gif\" alt=\"Campo obligatorio\"></label><input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "<input>" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REQUIRED_CONTROLS));
    }

    @Test
    public void evaluateLabelHiddenDisplayNone() throws Exception {
        checkAccessibility.setContent("<html><style type=\"text/css\">label { display: none; }</style>" +
                "<div>Los campos marcados con * son obligatorios</div>" +
                "<fieldset><legend>Grupo</legend>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\" />" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));
    }

    @Test
    public void evaluateLabelHiddenLeftNegative() throws Exception {
        checkAccessibility.setContent("<html><style>label { left: -9000px; }</style>" +
                "<div>Los campos marcados con * son obligatorios" +
                "<fieldset><legend>Grupo</legend>" +
                "<label for=\"id_1\">Lorem*</label><input>" +
                "<input id=\"id_1\">" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));

        checkAccessibility.setContent("<html><style>label { left: -999em; }</style>" +
                "<div>Los campos marcados con * son obligatorios" +
                "<fieldset><legend>Grupo</legend>" +
                "<label for=\"id_1\">Lorem*</label><input>" +
                "<input id=\"id_1\">" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));
    }

    @Test
    public void evaluateLabelHiddenLeftNegativeClass() throws Exception {
        checkAccessibility.setContent("<html><style>.hidden { left: -9000px; }</style>" +
                "<div>Los campos marcados con * son obligatorios" +
                "<fieldset><legend>Grupo</legend>" +
                "<label class=\"hidden\" for=\"id_1\">Lorem*</label><input>" +
                "<input id=\"id_1\">" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));
    }

    @Test
    public void evaluateLabelAndTitle() throws Exception {
        checkAccessibility.setContent("<html><style>.hidden { left: -9000px; }</style>" +
                "<div>Los campos marcados con * son obligatorios" +
                "<fieldset><legend>Grupo</legend>" +
                "<label class=\"hidden\" for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\" title=\"Grupo\">" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LABEL_CSS_HIDDEN));
    }


    @Test
    public void evaluateFormOneHeader() throws Exception {
        checkAccessibility.setContent("<html><form>" +
                "<h1>Foo</h1>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "</form>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_AS_LEGEND));
    }

    @Test
    public void evaluateFormTwoHeaders() throws Exception {
        checkAccessibility.setContent("<html><form>" +
                "<h2>Foo</h2>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "<h2>Bar</h2>" +
                "<label for=\"id_2\">Ipsum*</label>" +
                "<input id=\"id_2\">" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_AS_LEGEND));

        checkAccessibility.setContent("<html><form>" +
                "<fieldset><legend>Bogus</legend>" +
                "<h2>Foo</h2>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "<h2>Bar</h2>" +
                "<label for=\"id_2\">Ipsum*</label>" +
                "<input id=\"id_2\">" +
                "</fieldset>" +
                "</form>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_AS_LEGEND));
    }

    @Test
    public void evaluateSharepointFormName() throws Exception {
        checkAccessibility.setContent("<html><form name=\"aspnetForm\">" +
                "<h1>Foo</h1>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "<h2>Blah</h2>" +
                "<p>Lorem ipsum</p>" +
                "</form>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND_SHAREPOINT));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateSharepointFormId() throws Exception {
        checkAccessibility.setContent("<html><form id=\"aspnetForm\">" +
                "<h1>Foo</h1>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "<h2>Blah</h2>" +
                "<p>Lorem ipsum</p>" +
                "</form>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND_SHAREPOINT));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateSharepointFormIdAndName() throws Exception {
        checkAccessibility.setContent("<html><form id=\"aspnetForm\" method=\"post\" name=\"aspnetForm\">" +
                "<h1>Foo</h1>" +
                "<label for=\"id_1\">Lorem*</label>" +
                "<input id=\"id_1\">" +
                "<h2>Blah</h2>" +
                "<p>Lorem ipsum</p>" +
                "</form>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND_SHAREPOINT));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

//    @Test
//    public void evaluateURL() throws Exception {
//        checkAccessibility.setUrl("http://www.exteriores.gob.es/Portal/es/Paginas/inicio.aspx");
//        final Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND));
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_AS_LEGEND_SHAREPOINT));
//        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_3, TestUtils.OBS_VALUE_GREEN_ZERO);
//    }

}