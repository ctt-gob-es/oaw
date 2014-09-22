package es.inteco.intav;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.inteco.common.CheckAccessibility;
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

    public abstract int getId();

    protected CheckAccessibility getCheckAccessibility() {
        CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline("observatorio-inteco-1-0");
        checkAccessibility.setGuidelineFile("observatorio-inteco-1-0.xml");
        checkAccessibility.setLevel("aa");
        checkAccessibility.setUrl("http://www.example.org");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);

        return checkAccessibility;
    }

    protected int getNumProblems(List<Problem> problems) {
        int numProblems = 0;
        for (Problem problem : problems) {
            if (problem.getCheck().getId() == getId()) {
                numProblems++;
            }
        }
        return numProblems;
    }

}
