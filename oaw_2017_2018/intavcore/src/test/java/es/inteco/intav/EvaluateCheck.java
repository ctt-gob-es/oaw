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
