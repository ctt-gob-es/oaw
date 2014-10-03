package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CheckSelectOptgroupTest extends EvaluateCheck {

    private static final int SELECT_OPTGROUP_ID = 406;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
    }

    @Test
    public void evaluateFewOptions() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "<option>Opción 4</option>" +
                "<option>Opción 5</option>" +
                "<option>Opción 6</option>" +
                "<option>Opción 7</option>" +
                "<option>Opción 8</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, SELECT_OPTGROUP_ID));
    }

    @Test
    public void evaluateManyOptions() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SELECT_OPTGROUP_ID));
    }

    @Test
    public void evaluateManyOptionsGrouped() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción  1</option>" +
                "<option>Opción  2</option>" +
                "<option>Opción  3</option>" +
                "<option>Opción  4</option>" +
                "<option>Opción  5</option>" +
                "<option>Opción  6</option>" +
                "<option>Opción  7</option>" +
                "<option>Opción  8</option>" +
                "<option>Opción  9</option>" +
                "<optgroup  label=\"Grupo 1\"/>" +
                "<option>Opción 10</option>" +
                "<option>Opción 11</option>" +
                "<option>Opción 12</option>" +
                "<option>Opción 13</option>" +
                "<option>Opción 14</option>" +
                "<option>Opción 15</option>" +
                "<option>Opción 16</option>" +
                "<option>Opción 17</option>" +
                "<option>Opción 18</option>" +
                "<option>Opción 19</option>" +
                "<option>Opción 20</option>" +
                "<option>Opción 21</option>" +
                "<option>Opción 22</option>" +
                "<option>Opción 23</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, SELECT_OPTGROUP_ID));
    }

}
