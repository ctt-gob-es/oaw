package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
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
public final class Check_1_1_4_DataTableTest {
    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4 = "minhap.observatory.2.0.subgroup.1.1.4";

    /*<!-- Tabla de datos sin encabezados -->*/
    private static final int HEADERS = 86;

    /*<!-- Encabezamientos de tabla correctos -->*/
    private static final int  HEADERS_CORRECT = 116;

    /*<!-- Todas las tablas de datos contienen un elemento caption a menos que la tabla esté identificada en el documento. -->*/
    private static final int  CAPTION_EXISTS = 151;

    /*<!-- Celdas únicas que ocupan el ancho completo de la tabla para simular encabezamientos -->*/
    private static final int CELL_CAPTION =156;

    /*<!-- Ningún th está vacío. -->*/
    private static final int TH_BLANK= 159;
    /*<!-- Las tablas de datos que contienen más de una fila/columna de cabeceras usan los atributos id y headers para identificar las celdas. -->*/
    private static final int MISSING_ID_HEADERS_CHECK = 245;
    /*<!--Las tablas de datos que contienen más de una fila/columna de cabeceras presentan summary.-->*/
    private static final int COMPLEX_TABLE_SUMMARY = 418;
    private static final int TABLE_HEADINGS_ID = 415;
    /*<!-- Summary y Caption diferentes. -->*/
    private static final int SAME_CAPTION_SUMMARY_CHECK = 243;
    private  static final int MISSING_SCOPE_CHECK = 244;


    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateNoScopes() throws Exception {
        checkAccessibility.setContent("<table><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateScopesValid() throws Exception {
        checkAccessibility.setContent("<table><tr><th scope=\"col\">Header 1</th><th scope=\"row\">Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateScopesInvalid() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><th scope=\"bar\">Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(1, numProblems);
    }


    @Test
    public void evaluateSameCaptionAndSummary() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption>Same text</caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID));
    }

    @Test
    public void evaluateSameCaptionAndSummary2() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption><span><strong>Same</strong> text</span></caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexNoSummary() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexSummaryBlank() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexSummary() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Table summary\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateDataTable() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Relación alfabética de Convenios de nivel regional de Fundación CTIC CT\"><caption>\n" +
                "Convenios de ámbito regional de Fundación CTIC CT</caption>\n" +
                "<thead><tr><th>\n" +
                "ÁMBITO REGIONAL</th>\n" +
                "<th>\n" +
                "OBJETO DEL CONVENIO</th>\n" +
                "</tr></thead><tbody><tr><td>\n" +
                "Acuerdo de colaboración con PRODINTEC, ITMA y FICYT</td>\n" +
                "<td>\n" +
                "Regular los requisitos de la colaboración en la Prestación de Servicios a Entidades especializadas en el apoyo a empresas para su participación en Programa Marco I+D de la Unión Europea H2020</td>\n" +
                "</tr><tr class=\"odd\"><td>\n" +
                "APIA ( Asociación de Polígonos Industriales de Asturias) - <strong>Convenio Marco</strong></td>\n" +
                "<td>\n" +
                "Establecer el entorno de referencia para la acción coordinada entre las partes, intercambio de experiencias y conocimientos.</td>\n" +
                "</tr><tr><td>\n" +
                "ASATA (Agrupación de Sociedades Asturianas de trabajo asociado) - <strong>Convenio Marco</strong></td>\n" +
                "<td>\n" +
                "Convenio Marco de Colaboración en materia de Promoción del uso de las Tecnologías de la Información y la Comunicación en Iniciativas de Carácter Social.</td>\n" +
                "</tr><tr class=\"odd\"><td>\n" +
                "Asociación ASTARTE residuos solidarios - <strong>Convenio de colaboración</strong></td>\n" +
                "<td>\n" +
                "Convenio de colaboración entre la Asociación ASTARTE Residuos Solidarios y Fundación CTIC Centro Tecnológico para la recogida de residuos electrónicos RAEE</td>\n" +
                "</tr><tr><td>\n" +
                "Asociación Empresarial de Hostelería del Principado de Asturias – <strong>Convenio Marco</strong></td>\n" +
                "<td>\n" +
                "Establecer el entorno de referencia para la acción coordinada entre las partes intercambio de experiencias y conocimientos.</td>\n" +
                "</tr><tr class=\"odd\"><td>\n" +
                "Ayuntamiento de Gijón</td>\n" +
                "<td>\n" +
                "Desarrollo Económico TIC</td>\n" +
                "</tr></tbody></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }
    @Test
    public void evaluateDataTableRowSpan() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
        checkAccessibility.setContent("<table id=\"caracteristicas\" summary=\"Características del servicio de monitorización según los diferentes perfiles de contratación\">\n"+
                "\t\t\t\t<thead>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th>Característica</th>\n"+
                "\t\t\t\t\t\t<th>Perfil Básico</th>\n"+
                "\t\t\t\t\t\t<th>Perfil Medio</th>\n"+
                "\t\t\t\t\t\t<th>Perfil Avanzado</th>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t</thead>\n"+
                "\t\t\t\t<tfoot>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Más información</th>\n" +
                "\t\t\t\t\t\t<td colspan=\"3\"><a href=\"/tawmonitor/es/inicio.xhtml?actionMethod=es%2Finicio.xhtml%3Acontacto.solicitarInformacionContratacion&amp;cid=6701\" id=\"j_id114\">Solicitar más información sobre el servicio</a></td>\t\t\t\t\t\t\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</tfoot>"+
                "\t\t\t\t<tbody>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de dominios</th>\n"+
                "\t\t\t\t\t\t<td>1</td>\n"+
                "\t\t\t\t\t\t<td>5</td>\n"+
                "\t\t\t\t\t\t<td>10</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de análisis</th>\n"+
                "\t\t\t\t\t\t<td>1</td>\n"+
                "\t\t\t\t\t\t<td>5</td>\n"+
                "\t\t\t\t\t\t<td>10</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Periodo entre análisis</th>\n"+
                "\t\t\t\t\t\t<td>Quincenal, Mensual</td>\n"+
                "\t\t\t\t\t\t<td>Quincenal, Mensual</td>\n"+
                "\t\t\t\t\t\t<td>Semanal, Quincenal, Mensual</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de paginas</th>\n"+
                "\t\t\t\t\t\t<td>100</td>\n"+
                "\t\t\t\t\t\t<td>1000</td>\n"+
                "\t\t\t\t\t\t<td>3000</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de niveles</th>\n"+
                "\t\t\t\t\t\t<td>3</td>\n"+
                "\t\t\t\t\t\t<td>5</td>\n"+
                "\t\t\t\t\t\t<td>10</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de dominios analizados</th>\n"+
                "\t\t\t\t\t\t<td>1</td>\n"+
                "\t\t\t\t\t\t<td>3</td>\n"+
                "\t\t\t\t\t\t<td>5</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Análisis de subdominios</th>\n"+
                "\t\t\t\t\t\t<td>No</td>\n"+
                "\t\t\t\t\t\t\n"+
                "\t\t\t\t\t\t<td>Sí</td>\n"+
                "\t\t\t\t\t\t<td>Configurable por subdominio</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de urls en análisis de muestra</th>\n"+
                "\t\t\t\t\t\t<td>3</td>\n"+
                "\t\t\t\t\t\t<td>10</td>\n"+
                "\t\t\t\t\t\t<td>20</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr>\n"+
                "\t\t\t\t\t\t<th scope=\"row\">Envío de informe por ejecución de análisis</th>\n"+
                "\t\t\t\t\t\t<td>No</td>\n"+
                "\t\t\t\t\t\t<td>Único destinatario</td>\n"+
                "\t\t\t\t\t\t<td>Múltiples destinatarios</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n"+
                "\t\t\t\t\t\t<th>Envío de email de alarma</th>\n"+
                "\t\t\t\t\t\t<td>No</td>\n"+
                "\t\t\t\t\t\t<td>Sí</td>\n"+
                "\t\t\t\t\t\t<td>Sí</td>\n"+
                "\t\t\t\t\t</tr>\n"+
                "\t\t\t\t</tbody>\n"+
                "\t\t\t</table>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLayoutTable() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Table summary\"><tr><td>Header 1</td><td>Header 2</td></tr><tr><th><table><tr><td>Foo</td></tr></table></th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }


//    @Test
//    public void evaluateCTICTable() throws Exception {
//        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-inteco-1-0");
//        checkAccessibility.setUrl("http://www.fundacionctic.org/conocenos/acuerdos-y-colaboraciones");
//        Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 7));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 86));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 116));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 151));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 156));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 159));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 245));
//        TestUtils.checkVerificacion(evaluation, "inteco.observatory.subgroup.2.1.1", TestUtils.OBS_VALUE_GREEN_ONE);
//
//
//        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-inteco-1-0");
//        checkAccessibility.setUrl("http://monitor.fundacionctic.org/tawmonitor/es/inicio.xhtml");
//        evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 7));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 86));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 116));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 151));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 156));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 159));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 245));
//        TestUtils.checkVerificacion(evaluation, "inteco.observatory.subgroup.2.1.1", TestUtils.OBS_VALUE_GREEN_ONE);
//
//        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-inteco-1-0");
//        checkAccessibility.setUrl("http://www.fundacionctic.org/inicio");
//        Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 7));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 86));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 116));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 151));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 156));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 159));
//        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 245));
//        TestUtils.checkVerificacion(evaluation, "inteco.observatory.subgroup.2.1.1", TestUtils.OBS_VALUE_M_GREEN_ONE);
//    }


}
