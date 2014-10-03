package es.inteco.intav.checks;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CheckSimulatedListTest extends EvaluateCheck {

    private static final int P_SIMULATING_OL = 101;
    private static final int P_SIMULATING_UL = 120;
    private static final int BR_SIMULATING_UL = 121;
    private static final int BR_SIMULATING_OL = 150;
    private static final int UL_SIMULATING_OL_ID = 416;
    private static final int TABLE_SIMULATING_UL = 431;

    private CheckAccessibility checkAccessibility;

    @Before
    public void initCheck() {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateUlList() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateUlSimulatingOl() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "<li>4º) Opción 4</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateUlSimulatingOlRomanNumbers() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>i) Opción 1</li>" +
                "<li>ii) Opción 2</li>" +
                "<li>iii) Opción 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }


    @Test
    public void evaluateUlSimulatingOlLetters() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>a) Opción 1</li>" +
                "<li>b) Opción 2</li>" +
                "<li>c) Opción 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateSimulateOnly2Items() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "</ul>");
        // Permitimos hasta 2 elementos
        Assert.assertEquals(0, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateNotSortedItems() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>5º) Opción 5</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "<li>1º) Opción 1</li>" +
                "<li>4º) Opción 4</li>" +
                "</ul>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateOnly3Items() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateMixed() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3ª) Opción 3</li>" +
                "<li>4.) Opción 4</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateNotStartAtFirst() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>4º) Opción 1</li>" +
                "<li>5) Opción 2</li>" +
                "<li>6ª) Opción 3</li>" +
                "<li>7.) Opción 4</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluatePSimulatingUl() throws Exception {
        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" +
                "<p>* Opción 2</p>" +
                "<p>* Opción 3</p>" +
                "<p>* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_UL));

        checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>- Opción 3</p>" +
                "<p>- Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_UL));

        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>- Opción 3</p>" +
                "<p>* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_UL));

        checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>-) Opción 3</p>" +
                "<p>* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_UL));
    }

    @Test
    public void evaluateBrSimulatingUl() throws Exception {
        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" +
                "* Opción 2<br/>" +
                "* Opción 3<br/>" +
                "* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "- Opción 3<br/>" +
                "- Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "- Opción 3<br/>" +
                "* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "-) Opción 3<br/>" +
                "* Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_UL));
    }

    @Test
    public void evaluatePSimulatingOl() throws Exception {
        checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum</p>" +
                "<p>2) Opción 2</p>" +
                "<p>3) Opción 3</p>" +
                "<p>4) Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum</p>" +
                "<p>2- Opción 2</p>" +
                "<p>3- Opción 3</p>" +
                "<p>4- Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1º Opción 1 - Lorem ipsum</p>" +
                "<p>2º Opción 2</p>" +
                "<p>3) Opción 3</p>" +
                "<p>4. Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_OL));

        checkAccessibility.setContent("<p>3) Opción 1 - Lorem ipsum</p>" +
                "<p>4 Opción 2</p>" +
                "<p>5) Opción 3</p>" +
                "<p>6 Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, P_SIMULATING_OL));
    }

    @Test
    public void evaluateBrSimulatingOl() throws Exception {
        checkAccessibility.setContent("<p>1. Opción 1 - Lorem ipsum<br/>" +
                "2. Opción 2<br/>" +
                "3. Opción 3<br/>" +
                "4. Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum<br/>" +
                "2- Opción 2<br/>" +
                "3- Opción 3<br/>" +
                "4- Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum<br/>" +
                "2º Opción 2<br/>" +
                "3. Opción 3<br/>" +
                "4- Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>4) Opción 1 - Lorem ipsum<br/>" +
                "5- Opción 2<br/>" +
                "6 Opción 3<br/>" +
                "7-) Opción 4</p>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, BR_SIMULATING_OL));
    }

    @Test
    public void evaluateTableSimulatingUl() throws Exception {
        checkAccessibility.setContent("<table><tr><td>Item 1</td><td>Item 2</td></tr><tr><td>Item 3</td><td>Item 4</td></tr></table");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr></table");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1 - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet laoreet sapien. Morbi ultricies erat elit, sit amet posuere velit posuere.</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr><tr><td>Item 4</td></tr></table");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1 - <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em></td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, TABLE_SIMULATING_UL));
    }

}

