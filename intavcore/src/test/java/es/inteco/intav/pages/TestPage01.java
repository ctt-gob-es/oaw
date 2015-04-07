package es.inteco.intav.pages;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluatePage;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TestPage01 extends EvaluatePage {

    private final Map<String, Number> VALORES = new HashMap<String, Number>() {
        {
            put("score", new BigDecimal(9.40));
            put("observatory.aspect.navigation", new BigDecimal(8));
            put("observatory.aspect.general", BigDecimal.TEN);
            put("observatory.aspect.structure", BigDecimal.TEN);
            put("observatory.aspect.alternatives", BigDecimal.TEN);
            put("observatory.aspect.presentation", BigDecimal.TEN);

            put("Priority 1", BigDecimal.TEN);
            put("Priority 1 A", BigDecimal.TEN);
            put("Priority 1 AA", BigDecimal.TEN);
            put("Priority 2", new BigDecimal(8.80));
            put("Priority 2 A", BigDecimal.TEN);
            put("Priority 2 AA", new BigDecimal(8.00));

            put("inteco.observatory.subgroup.1.1.1", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.1.2", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.1.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.1.4", OBS_VALUE_GREEN_ONE);

            put("inteco.observatory.subgroup.1.2.1", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.2.2", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.2.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.2.4", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.2.5", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.1.2.6", OBS_VALUE_GREEN_ONE);

            put("inteco.observatory.subgroup.2.1.1", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.2.1.2", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.1.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.1.4", OBS_VALUE_GREEN_ONE);

            put("inteco.observatory.subgroup.2.2.1", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.2.2.2", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.2.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.2.4", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.2.5", OBS_VALUE_GREEN_ZERO);
            put("inteco.observatory.subgroup.2.2.6", OBS_VALUE_GREEN_ONE);
        }
    };

    @Test
    public void test() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("test1.html");
        checkAccessibility.setGuideline("observatorio-inteco-1-0");
        checkAccessibility.setGuideline("observatorio-inteco-1-0.xml");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);

        checkEvaluation(oef, VALORES);
    }

}
