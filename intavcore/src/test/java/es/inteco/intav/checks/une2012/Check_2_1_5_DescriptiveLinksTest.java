/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
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
public final class Check_2_1_5_DescriptiveLinksTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5 = "minhap.observatory.2_0.subgroup.2.1.5";

    private final int TOO_MUCH_TEXT_LINKS = 181;
    private final int NO_DESCRIPTIVE_TEXT_LINKS = 79;
    private final int BLANK_TEXT_LINKS = 69;
    private final int REDUNDANT_IMG_ALT_TEXT_LINKS = 428;

    private CheckAccessibility checkAccessibility;
    private Evaluation evaluation;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateImgNoAlt() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"/foo\"><img /></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        // Caso especial ya que una imagen sin alt se contabiliza en la verificación 1.1.1
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><body><a href=\"/foo\"><img /><img /></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        // Caso especial ya que una imagen sin alt se contabiliza en la verificación 1.1.1
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateImgAltBlank() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\"><img alt=\"\"/></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a href=\"foo.html\"><img alt=\"&nbsp;\"/></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a href=\"foo.html\"><img alt=\" \"/></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"/es/gobierno/representantes/sa-da-maria-jesus-pinto-caballero\"><img alt=\"\" height=\"220\" src=\"http://www.benidorm.org/sites/default/files/styles/medium/public/pinto2_webok.jpg?itok=k0OT1d_0\" typeof=\"foaf:Image\" width=\"220\"/></a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }


    @Test
    public void evaluateSuspiciousLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Haz click aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Pincha aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Pinche aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet.</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLeyLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Real Decreto Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet. </a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateWhiteSpacesAreTrimmedOnLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\">          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet.          </a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateInnerWhiteSpacesAreSkippedOnLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\">Lorem  ipsum    dolor   sit    amet,    consectetur    adipiscing elit.  Sed    ultricies    ante eget    lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet.</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }


    @Test
    public void evaluateLongLinkImgAltText() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"foo.html\"><img alt=\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet.\"/> </a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateWhiteSpacesOnRedundantLinks() throws Exception {
        checkAccessibility.setContent("<a href=\"foo.html\">Lorem <img alt=\"Lorem\"/></a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><img alt=\"RSS\" title=\"RSS\" /> RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><img alt=\"RSS\" title=\"RSS\" />     RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><img alt=\"RSS\" title=\"RSS\" />&nbsp;RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><img alt=\"RSS \" title=\"RSS\" /> RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateInlineTags() throws Exception {
        checkAccessibility.setContent("<a href=\"foo.html\"><strong>Lorem</strong> <img alt=\"Lorem\"/></a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><strong>L</strong>orem <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a href=\"foo.html\"><strong><em>Lorem</em></strong> <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateExcepcionTextoReglamentoCE() throws Exception {
        //http://www.idi.mineco.gob.es/portal/site/MICINN/menuitem.7eeac5cd345b4f34f09dfd1001432ea0/?vgnextoid=ccdc6e8c792b9210VgnVCM1000001d04140aRCRD
        checkAccessibility.setContent("<a href=\"http://google.com\">Reglamento (CE) Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
    }

}

