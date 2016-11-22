package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckTables;
import ca.utoronto.atrc.tile.accessibilitychecker.CheckUtils;
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
public final class Check_1_1_4_DataTableTest {
    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4 = "minhap.observatory.2_0.subgroup.1.1.4";

    /*<!-- Tabla de datos sin encabezados -->*/
    private static final int HEADERS = 86;

    /*<!-- Encabezamientos de tabla correctos -->*/
    private static final int HEADERS_CORRECT = 116;

    /*<!-- Todas las tablas de datos contienen un elemento caption a menos que la tabla esté identificada en el documento. -->*/
    private static final int CAPTION_OR_SUMMARY_EXISTS = 151;

    /*<!-- Celdas únicas que ocupan el ancho completo de la tabla para simular encabezamientos -->*/
    private static final int CELL_CAPTION = 156;

    /*<!-- Ningún th está vacío. -->*/
    private static final int TH_BLANK = 159;
    /*<!-- Las tablas de datos que contienen más de una fila/columna de cabeceras usan los atributos id y headers para identificar las celdas. -->*/
    private static final int MISSING_ID_HEADERS = 245;
    /*<!--Las tablas de datos que contienen más de una fila/columna de cabeceras presentan summary.-->*/
    private static final int COMPLEX_TABLE_SUMMARY = 418;
    /*<!-- Summary y Caption diferentes. -->*/
    private static final int SAME_CAPTION_SUMMARY_CHECK = 243;
    private static final int MISSING_SCOPE_CHECK = 415;
    // Encabezados simulando captions
    private static final int HEADER_AS_CAPTION = 464;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateNoScopes() throws Exception {
        checkAccessibility.setContent("<table><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
    }

    @Test
    public void evaluateScopesValid() throws Exception {
        checkAccessibility.setContent("<table><tr><th scope=\"col\">Header 1</th><th scope=\"row\">Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
    }

    @Test
    public void evaluateScopesInvalid() throws Exception {
        checkAccessibility.setContent("<html><body><table><tr><th scope=\"bar\">Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
    }


    @Test
    public void evaluateSameCaptionAndSummary() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption>Same text</caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
    }

    @Test
    public void evaluateSameCaptionAndSummary2() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption><span><strong>Same</strong> text</span></caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateCellCaption() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><tr><th colspan=\"2\">Caption</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));

        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><tr><td colspan=\"2\">Caption</td></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
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
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateTableComplexSummary() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Table summary\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
    }


    @Test
    public void evaluateTableComplexCaption() throws Exception {
        checkAccessibility.setContent("<html><body><table><caption>Table caption</caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
    }

    @Test
    public void evaluateTableComplexCaptionBlank() throws Exception {
        checkAccessibility.setContent("<html><body><table><caption></caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
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
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateDataTableRowSpan() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
        checkAccessibility.setContent("<table id=\"caracteristicas\" summary=\"Características del servicio de monitorización según los diferentes perfiles de contratación\">\n" +
                "\t\t\t\t<thead>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th>Característica</th>\n" +
                "\t\t\t\t\t\t<th>Perfil Básico</th>\n" +
                "\t\t\t\t\t\t<th>Perfil Medio</th>\n" +
                "\t\t\t\t\t\t<th>Perfil Avanzado</th>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</thead>\n" +
                "\t\t\t\t<tfoot>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Más información</th>\n" +
                "\t\t\t\t\t\t<td colspan=\"3\"><a href=\"/tawmonitor/es/inicio.xhtml?actionMethod=es%2Finicio.xhtml%3Acontacto.solicitarInformacionContratacion&amp;cid=6701\" id=\"j_id114\">Solicitar más información sobre el servicio</a></td>\t\t\t\t\t\t\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</tfoot>" +
                "\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de dominios</th>\n" +
                "\t\t\t\t\t\t<td>1</td>\n" +
                "\t\t\t\t\t\t<td>5</td>\n" +
                "\t\t\t\t\t\t<td>10</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de análisis</th>\n" +
                "\t\t\t\t\t\t<td>1</td>\n" +
                "\t\t\t\t\t\t<td>5</td>\n" +
                "\t\t\t\t\t\t<td>10</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Periodo entre análisis</th>\n" +
                "\t\t\t\t\t\t<td>Quincenal, Mensual</td>\n" +
                "\t\t\t\t\t\t<td>Quincenal, Mensual</td>\n" +
                "\t\t\t\t\t\t<td>Semanal, Quincenal, Mensual</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de paginas</th>\n" +
                "\t\t\t\t\t\t<td>100</td>\n" +
                "\t\t\t\t\t\t<td>1000</td>\n" +
                "\t\t\t\t\t\t<td>3000</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de niveles</th>\n" +
                "\t\t\t\t\t\t<td>3</td>\n" +
                "\t\t\t\t\t\t<td>5</td>\n" +
                "\t\t\t\t\t\t<td>10</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de dominios analizados</th>\n" +
                "\t\t\t\t\t\t<td>1</td>\n" +
                "\t\t\t\t\t\t<td>3</td>\n" +
                "\t\t\t\t\t\t<td>5</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Análisis de subdominios</th>\n" +
                "\t\t\t\t\t\t<td>No</td>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t<td>Sí</td>\n" +
                "\t\t\t\t\t\t<td>Configurable por subdominio</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Número máximo de urls en análisis de muestra</th>\n" +
                "\t\t\t\t\t\t<td>3</td>\n" +
                "\t\t\t\t\t\t<td>10</td>\n" +
                "\t\t\t\t\t\t<td>20</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<th scope=\"row\">Envío de informe por ejecución de análisis</th>\n" +
                "\t\t\t\t\t\t<td>No</td>\n" +
                "\t\t\t\t\t\t<td>Único destinatario</td>\n" +
                "\t\t\t\t\t\t<td>Múltiples destinatarios</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr class=\"fila_alterna\">\n" +
                "\t\t\t\t\t\t<th>Envío de email de alarma</th>\n" +
                "\t\t\t\t\t\t<td>No</td>\n" +
                "\t\t\t\t\t\t<td>Sí</td>\n" +
                "\t\t\t\t\t\t<td>Sí</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</tbody>\n" +
                "\t\t\t</table>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        //Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
    }

    @Test
    public void evaluateLayoutTable() throws Exception {
        checkAccessibility.setContent("<html><body><table summary=\"Table summary\"><tr><td>Header 1</td><td>Header 2</td></tr><tr><th><table><tr><td>Foo</td></tr></table></th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateRealExampleTable() throws Exception {
        checkAccessibility.setContent("<html>" +
                "<table summary=\"Esta tabla proporciona información sobre el número total de\n" +
                "solicitudes registradas, proyectos aprobados y financiación concedida en Europa\n" +
                "investigación y Europa Redes y Gestores\">\n" +
                "<caption>Acciones en Participación en Europa investigación y Europa Redes y Gestores</caption>\n" +
                "        <thead>\n" +
                "                <tr class=\"tituloTabla\">\n" +
                "                        <td>&#160;</td>\n" +
                "                        <th scope=\"col\">Solicitudes totales registradas</th>\n" +
                "                        <th scope=\"col\">Proyectos concedidos</th>\n" +
                "                        <th scope=\"col\">Financiación concedida</th>\n" +
                "                </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "                <tr>\n" +
                "                        <th scope=\"row\">Europa Investigación</th>\n" +
                "                        <td>192</td>\n" +
                "                        <td>142</td>\n" +
                "                        <td>3.000€</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                        <th scope=\"row\">Europa Redes y Gestores</th>\n" +
                "                        <td>139</td>\n" +
                "                        <td>34</td>\n" +
                "                        <td>5.000€</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "</table></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        // Esta tabla se considera compleja y necesita la combinación ID, HEADERS
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateRealExampleTableIdHeadersBlankTD() throws Exception {
        checkAccessibility.setContent("<html>" +
                "<table summary=\"Esta tabla proporciona información sobre el número total de\n" +
                "solicitudes registradas, proyectos aprobados y financiación concedida en Europa\n" +
                "investigación y Europa Redes y Gestores\">\n" +
                "<caption>Acciones en Participación en Europa investigación y Europa Redes y Gestores</caption>\n" +
                "        <thead>\n" +
                "                <tr class=\"tituloTabla\">\n" +
                "                        <td>&#160;</td>\n" +
                "                        <th id=\"solicitudes\">Solicitudes totales registradas</th>\n" +
                "                        <th id=\"proyectos\">Proyectos concedidos</th>\n" +
                "                        <th id=\"financiacion\">Financiación concedida</th>\n" +
                "                </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "                <tr>\n" +
                "                        <th id=\"investigacion\">Europa Investigación</th>\n" +
                "                        <td headers=\"investigacion solicitudes\">192</td>\n" +
                "                        <td headers=\"investigacion redes\">142</td>\n" +
                "                        <td headers=\"investigacion financiacion\">3.000€</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                        <th id=\"redes\">Europa Redes y Gestores</th>\n" +
                "                        <td headers=\"redes solicitudes\">139</td>\n" +
                "                        <td headers=\"redes proyectos\">34</td>\n" +
                "                        <td headers=\"redes financiacion\">5.000€</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "</table></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateRealExampleTableIdHeadersBlankTH() throws Exception {
        checkAccessibility.setContent("<html>" +
                "<table summary=\"Esta tabla proporciona información sobre el número total de\n" +
                "solicitudes registradas, proyectos aprobados y financiación concedida en Europa\n" +
                "investigación y Europa Redes y Gestores\">\n" +
                "<caption>Acciones en Participación en Europa investigación y Europa Redes y Gestores</caption>\n" +
                "        <thead>\n" +
                "                <tr class=\"tituloTabla\">\n" +
                "                        <th>&#160;</th>\n" +
                "                        <th id=\"solicitudes\">Solicitudes totales registradas</th>\n" +
                "                        <th id=\"proyectos\">Proyectos concedidos</th>\n" +
                "                        <th id=\"financiacion\">Financiación concedida</th>\n" +
                "                </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "                <tr>\n" +
                "                        <th id=\"investigacion\">Europa Investigación</th>\n" +
                "                        <td headers=\"investigacion solicitudes\">192</td>\n" +
                "                        <td headers=\"investigacion redes\">142</td>\n" +
                "                        <td headers=\"investigacion financiacion\">3.000€</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                        <th id=\"redes\">Europa Redes y Gestores</th>\n" +
                "                        <td headers=\"redes solicitudes\">139</td>\n" +
                "                        <td headers=\"redes proyectos\">34</td>\n" +
                "                        <td headers=\"redes financiacion\">5.000€</td>\n" +
                "                </tr>\n" +
                "        </tbody>\n" +
                "</table></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }


    @Test
    public void evaluateHeaderLayoutTable() throws Exception {
        checkAccessibility.setContent("<html>" +
                "<h2>foo</h2>" +
                "<table>\n" +
                "   <tbody>\n" +
                "       <tr>\n" +
                "           <td>192</td>\n" +
                "           <td>142</td>\n" +
                "           <td>3.000€</td>\n" +
                "       </tr>\n" +
                "       <tr>\n" +
                "           <td>139</td>\n" +
                "           <td>34</td>\n" +
                "           <td>5.000€</td>\n" +
                "       </tr>\n" +
                "<tr><table><tr><td>\n" +
                "\n" +
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vitae massa felis. Nam euismod ac dui vitae interdum. Aenean id nibh semper, venenatis velit nec, lacinia ex. Nunc condimentum arcu at dictum ultricies. Nullam sed fringilla eros, a iaculis purus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed ullamcorper lobortis pharetra. Donec a nunc non leo posuere fermentum ac non leo. Suspendisse porttitor dignissim suscipit. Vestibulum finibus nec ligula eu blandit.</p>\n" +
                "\n" +
                "<p>Donec fermentum laoreet lacinia. Donec placerat ultrices arcu, sed eleifend quam iaculis sit amet. Nulla fermentum pellentesque sodales. Praesent eget laoreet arcu. Curabitur facilisis quam sem, ac tincidunt augue porttitor ut. Nullam eget justo dui. Aliquam in sodales quam.</p>\n" +
                "\n" +
                "<p>Etiam fermentum accumsan dui, vulputate elementum diam aliquet eget. Nam efficitur dolor pretium tempor tincidunt. Mauris nulla arcu, sagittis at dignissim finibus, rutrum a mi. Aenean tincidunt felis ac rutrum ullamcorper. Quisque erat nibh, tristique eget semper eu, tempus ac quam. Vivamus vitae risus.</p></td></tr></table></tr>" +
                "    </tbody>\n" +
                "</table></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateHeaderDataTableWithCaption() throws Exception {
        checkAccessibility.setContent("<html>" +
                "<h2>foo</h2>" +
                "<table>" +
                "   <caption>Foo bar</caption>\n" +
                "   <thead>\n" +
                "       <tr>\n" +
                "           <th scope=\"col\">Solicitudes totales registradas</th>\n" +
                "           <th scope=\"col\">Proyectos concedidos</th>\n" +
                "           <th scope=\"col\">Financiación concedida</th>\n" +
                "       </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "       <tr>\n" +
                "           <td>192</td>\n" +
                "           <td>142</td>\n" +
                "           <td>3.000€</td>\n" +
                "       </tr>\n" +
                "       <tr>\n" +
                "           <td>139</td>\n" +
                "           <td>34</td>\n" +
                "           <td>5.000€</td>\n" +
                "       </tr>\n" +
                "    </tbody>\n" +
                "</table></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateHeaderDataTableWithoutCaption() throws Exception {
        final String dataTableWithoutCaption = "<table summary=\"Foo bar\">" +
                "   <thead>\n" +
                "       <tr>\n" +
                "           <th scope=\"col\">Solicitudes totales registradas</th>\n" +
                "           <th scope=\"col\">Proyectos concedidos</th>\n" +
                "           <th scope=\"col\">Financiación concedida</th>\n" +
                "       </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "       <tr>\n" +
                "           <td>192</td>\n" +
                "           <td>142</td>\n" +
                "           <td>3.000€</td>\n" +
                "       </tr>\n" +
                "       <tr>\n" +
                "           <td>139</td>\n" +
                "           <td>34</td>\n" +
                "           <td>5.000€</td>\n" +
                "       </tr>\n" +
                "    </tbody>\n" +
                "</table>";

        checkAccessibility.setContent("<h1>Foo</h1>" + dataTableWithoutCaption);
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<h2>Foo</h2>" + dataTableWithoutCaption);
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<h3>Foo</h3>" + dataTableWithoutCaption);
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<h4>Foo</h4>" + dataTableWithoutCaption);
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<h5>Foo</h5>" + dataTableWithoutCaption);
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<h6>Foo</h6>" + dataTableWithoutCaption);
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateNoSiblingHeaderDataTableWithoutCaption() throws Exception {
        checkAccessibility.setContent("<h1>Foo</h1>Lorem ipsum" + DATA_TABLE_WITHOUT_CAPTION_HTML);
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNestedTables() throws Exception {
        final String tablas = "<table id='externa'>\n" +
                "  <tr>\n" +
                "    <td>Lorem ipsum 1</td>\n" +
                "     <td>Lorem ipsum 2</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Lorem ipsum\n" +
                "            <table id='interna' summary='summary'>\n" +
                "              <tr><th>Header One</th><th>Header Two</th></tr>\n"+
                "              <tr>\n" +
                "                    <td>Lorem ipsum a</td>\n" +
                "                    <td>Lorem ipsum b</td>\n" +
                "               </tr>\n" +
                "          <tr>\n" +
                "            <td>Lorem ipsum I</td>\n" +
                "            <td>Lorem ipsum II</td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "    </td>\n" +
                "    <td>Lorem ipsum æ</td>\n" +
                "  </tr>\n" +
                "</table>";
        checkAccessibility.setContent(tablas);
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        TestUtils.printProblems(evaluation.getProblems(), HEADERS);
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_CORRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CAPTION_OR_SUMMARY_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CELL_CAPTION));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TH_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_ID_HEADERS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_TABLE_SUMMARY));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MISSING_SCOPE_CHECK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADER_AS_CAPTION));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    public static final String DATA_TABLE_WITHOUT_CAPTION_HTML = "<table summary=\"Foo bar\">" +
            "   <thead>\n" +
            "       <tr>\n" +
            "           <th scope=\"col\">Solicitudes totales registradas</th>\n" +
            "           <th scope=\"col\">Proyectos concedidos</th>\n" +
            "           <th scope=\"col\">Financiación concedida</th>\n" +
            "       </tr>\n" +
            "   </thead>\n" +
            "   <tbody>\n" +
            "       <tr>\n" +
            "           <td>192</td>\n" +
            "           <td>142</td>\n" +
            "           <td>3.000€</td>\n" +
            "       </tr>\n" +
            "       <tr>\n" +
            "           <td>139</td>\n" +
            "           <td>34</td>\n" +
            "           <td>5.000€</td>\n" +
            "       </tr>\n" +
            "    </tbody>\n" +
            "</table>";
}
