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
public class TestPage03 extends EvaluatePage {

    private final Map<String, Number> VALORES = new HashMap<String, Number>() {
        {
            put("score", new BigDecimal(5));

            put("Priority 1", new BigDecimal(5));

            put("Priority 1 A", new BigDecimal(6.7));
            put("inteco.observatory.subgroup.1.1.1", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.1.1.2", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.1.1.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.1.4", OBS_VALUE_GREEN_ONE);

            put("Priority 1 AA", new BigDecimal(4));
            put("inteco.observatory.subgroup.1.2.1", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.1.2.2", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.1.2.3", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.1.2.4", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.1.2.5", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.1.2.6", OBS_VALUE_GREEN_ONE);

            put("Priority 2", new BigDecimal(5));

            put("Priority 2 A", new BigDecimal(10));
            put("inteco.observatory.subgroup.2.1.1", OBS_VALUE_NOT_SCORE);
            put("inteco.observatory.subgroup.2.1.2", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.1.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.1.4", OBS_VALUE_NOT_SCORE);

            put("Priority 2 AA", new BigDecimal(3.3));
            put("inteco.observatory.subgroup.2.2.1", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.2.2.2", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.2.2.3", OBS_VALUE_GREEN_ONE);
            put("inteco.observatory.subgroup.2.2.4", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.2.2.5", OBS_VALUE_RED_ZERO);
            put("inteco.observatory.subgroup.2.2.6", OBS_VALUE_GREEN_ONE);

            put("observatory.aspect.navigation", new BigDecimal(6));
            put("observatory.aspect.general", new BigDecimal(7.5));
            put("observatory.aspect.structure", new BigDecimal(3.3));
            put("observatory.aspect.alternatives", new BigDecimal(5));
            put("observatory.aspect.presentation", new BigDecimal(0));
        }
    };

    @Test
    public void test() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("test3.html");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);

        checkEvaluation(oef, VALORES);
    }

}
