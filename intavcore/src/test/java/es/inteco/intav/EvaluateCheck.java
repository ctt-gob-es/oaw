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
package es.inteco.intav;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.BeforeClass;

import java.util.List;

/**
 *
 */
public abstract class EvaluateCheck {

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    protected CheckAccessibility getCheckAccessibility(final String guideline) {
        CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline(guideline);
        checkAccessibility.setGuidelineFile(guideline + ".xml");
        checkAccessibility.setLevel("a");
        checkAccessibility.setUrl("http://www.example.org");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);

        return checkAccessibility;
    }

    protected int getNumProblems(final CheckAccessibility checkAccessibility, int idCheck) {
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        return getNumProblems(evaluation.getProblems(), idCheck);
    }

    protected int getNumProblems(final List<Problem> problems, int idCheck) {
        int numProblems = 0;
        for (Problem problem : problems) {
            if (problem.getCheck().getId() == idCheck) {
                numProblems++;
            }
        }
        return numProblems;
    }

}
