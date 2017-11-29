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
public final class Check_2_1_6_ContextChangesTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_6 = "minhap.observatory.2_0.subgroup.2.1.6";

    private static final int ONFOCUS_ONBLUR_CHANGE = 452;
    private static final int ONLOAD_CONTEXT_CHANGE = 453;
    private static final int SELECT_CONTEXT_CHANGE = 454;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateChangeContextOnFocus() throws Exception {
        checkAccessibility.setContent("<html><input onfocus=\"javascript:window.location=http://www.google.es\">Lorem ipsum</p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ONFOCUS_ONBLUR_CHANGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_6, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><input onblur=\"javascript:window.location=http://www.google.es\">Lorem ipsum</p></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ONFOCUS_ONBLUR_CHANGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateChangeContextOnLoad() throws Exception {
        checkAccessibility.setContent("<html><body onload=\"window.location=http://www.google.es\"><p>Lorem ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ONLOAD_CONTEXT_CHANGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateChangeContextOnChange() throws Exception {
        checkAccessibility.setContent("<html><select onchange=\"javascript:window.focus\">Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SELECT_CONTEXT_CHANGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_6, TestUtils.OBS_VALUE_RED_ZERO);
    }

}