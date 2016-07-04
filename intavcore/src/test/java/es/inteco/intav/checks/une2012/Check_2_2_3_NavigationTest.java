package es.inteco.intav.checks.une2012;

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
public final class Check_2_2_3_NavigationTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3 = "minhap.observatory.2_0.subgroup.2.2.3";

    private static final int BROKEN_DOMAIN_LINKS_WARNING = 455;
    private static final int MORE_THAN_ONE_BROKEN_DOMAIN_LINKS = 456;

    private static final int BROKEN_EXTERNAL_LINKS_WARNING = 457;
    private static final int MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS = 458;

    private static final int COMBINED_ADJACENT_LINKS = 180;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateNoLinks() throws Exception {
        checkAccessibility.setContent("<html><p></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateAdjacentSameLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a> <a href=\"http://www.google.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateAdjacentNoSameLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a> <a href=\"http://www.google.com\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateOnlyOneBrokenDomainsLinks() throws Exception {
        checkAccessibility.setUrl("http://www.zxcvb.es");
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateMultipleBrokenDomainsLinks() throws Exception {
        checkAccessibility.setUrl("http://www.zxcvb.es");
        checkAccessibility.setContent("<html><p><a href=\"http://www.zxcvb.es/path\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateOnlyOneBrokenExternalLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateTwoBrokenExternalLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.zxcvb.es/path\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateMultipleBrokenExternalLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.zxcvb.es/path\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p><p><a href=\"http://www.asaszxzx.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(3, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(3, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateForbiddenLink() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"https://www.fundacionctic.org/user/1\">Lorem</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateMixedBrokenLink() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"https://www.fundacionctic.org/user/1\">Lorem</a>" +
                "<a href=\"aa.html\">enlace aa</a>" +
                "<a href=\"bb.html\">enlace bb</a>" +
                "<a href=\"http://www.noexiste_deverdad.es/no_existe.html\">enlace no existe</a></li></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    /*@Test
    public void evaluateRealPage() throws Exception {
        connection.addRequestProperty("Cookie", "_ga=GA1.3.1484412100.1434467914; ASP.NET_SessionId=fm5nehixj2k3ssiaqjbbzct4");
        // Este enlace necesita la cookie anterior para conectarse si no redirecciona y se queda en un bucle infinito
        checkAccessibility.setContent("<html><p>" +
                "<a href=\"https://serviciostelematicos.minhap.gob.es/apps/virtuallib/publica/Pestanias/Filtro.aspx?Pestania=34\">enlace</a>"+
                "</p></html>");

        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        TestUtils.printProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING);

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_EXTERNAL_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_TWO_BROKEN_EXTERNAL_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_DOMAIN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_DOMAIN_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    } //*/

}