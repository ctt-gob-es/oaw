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
public final class Check_2_1_1_JavaScriptAccesibleTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1 = "minhap.observatory.2_0.subgroup.2.1.1";
    private static final int DUPLICATED_DEPENDENT_EVENTS = 160;
    private static final int ELEMENTS_INTERACTIVE = 432;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateAOnClick() throws Exception {
        checkAccessibility.setContent("<html><p><a onclick=\"foo()\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateImgNoRoleNoTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" /></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgRoleNoTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" role=\"link\"/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgRoleTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" role=\"link\" tabindex=\"2\"/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateMouseOver() throws Exception {
        checkAccessibility.setContent("<html><p><a onfocus=\"foo()\" onmouseover=\"bar()\">Lorem ipsum</a></p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onfocus=\"foo()\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onmouseover=\"\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onmouseover=\"bar()\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateMouseOut() throws Exception {
        checkAccessibility.setContent("<html><p><a onblur=\"foo()\" onmouseout=\"bar()\">Lorem ipsum</a></p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onblur=\"foo()\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onmouseout=\"\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);

        checkAccessibility.setContent("<html><p><a onmouseout=\"bar()\">Lorem ipsum</a></p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluteMixedCode() throws Exception {
        checkAccessibility.setContent("<li class=\" submenu\" id=\"menu104\"" +
                "        onblur=\"clearTimeout(del04)\"\n" +
                "        onclick=\"despliegaMenu(menu104,2,2)\"\n" +
                "        onfocus=\"del04=setTimeout(function(){despliegaMenu(menu104,2,2)},400);\"\n" +
                "        onkeypress=\"despliegaMenu(menu104,2,2)\"\n" +
                "        onmouseout=\"clearTimeout(del04)\"\n" +
                "        onmouseover=\"del04=setTimeout(function(){despliegaMenu(menu104,2,2)},400);\"\n" +
                "        role=\"menuitem\" tabindex=\"-1\">foo</li>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void testOnclickDivRequieresRole() {
        checkAccessibility.setContent("<DIV class=\"wtitulo\" onclick=\"$('#widget1').hide()\" style=\"cursor:\n" +
                "pointer; height: 30px; width: 100%; display: table-row\" tabindex=\"0\">Cerrar</div>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void testOnclickDivWithRole() {
        checkAccessibility.setContent("<DIV class=\"wtitulo\" onclick=\"$('#widget1').hide()\" style=\"cursor:\n" +
                "pointer; height: 30px; width: 100%; display: table-row\" tabindex=\"0\" role=\"button\">Cerrar</div>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DUPLICATED_DEPENDENT_EVENTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }
}

