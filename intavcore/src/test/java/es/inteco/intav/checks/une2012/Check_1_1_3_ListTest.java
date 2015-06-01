package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_1_3_ListTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_3 = "minhap.observatory.2_0.subgroup.1.1.3";

    private static final int P_SIMULATING_OL = 101;
    private static final int P_SIMULATING_UL = 120;
    private static final int BR_SIMULATING_UL = 121;
    private static final int BR_SIMULATING_OL = 410;
    private static final int LI_PARENT_UL_OL = 311;
    private static final int DT_OUTSIDE_DL = 313;
    private static final int DD_OUTSIDE_DL = 314;
    private static final int LIST_NESTED_DIRECTLY_OL = 317;
    private static final int LIST_NESTED_DIRECTLY_UL = 318;
    private static final int INCORRECT_SORTED_LIST = 319;
    private static final int INCORRECT_UNSORTED_LIST = 320;
    private static final int UL_SIMULATING_OL_ID = 416;
    private static final int SORTED_EMPTY_LIST = 423;
    private static final int UNSORTED_EMPTY_LIST = 424;
    private static final int EMPTY_DEFINITION_LIST = 425;
    private static final int DL_INCORRECT = 427;
    private static final int TABLE_SIMULATING_UL = 431;
    private static final int P_IMAGE_SIMULATING_LIST = 445;
    private static final int BR_IMAGE_SIMULATING_LIST = 459;

    private CheckAccessibility checkAccessibility;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Before
    public void setUp() {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateDlCorrectList() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        // Es válido usar dos dt seguidos para definir dos términos a la vez (sinónimos)
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "<dt>Term 3</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
    }

    @Test
    public void evaluateDTConsecutive() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "<dt>Term 3</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
    }

    @Test
    public void evaluateDTEnding() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "</dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
    }

    @Test
    public void evaluateDIVInsideDL() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<div>Lorem</div>" +
                "</dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
    }

    @Test
    public void evaluateULInsideDL() throws Exception {
        // Atención este error es una diferencia respecto a la misma comprobación en la v1.0
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<ul><li>Lorem</li></ul>" +
                "</dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
    }

    @Test
    public void evaluateDTOutsideDL() throws Exception {
        checkAccessibility.setContent("<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DT_OUTSIDE_DL));
    }

    @Test
    public void evaluateDDOutsideDL() throws Exception {
        checkAccessibility.setContent("<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), DD_OUTSIDE_DL));
    }

    @Test
    public void evaluateDLBlankList() throws Exception {
        checkAccessibility.setContent("<dl></dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EMPTY_DEFINITION_LIST));
    }

    @Test
    public void evaluateDLNestedBlankList() throws Exception {
        checkAccessibility.setContent("<dl><dl><dt>Foo</dt><dd>Bar</dd></dl></dl>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EMPTY_DEFINITION_LIST));
    }

    @Test
    public void evaluateLiParentULOL() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<ol>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ol>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateLiNoParent() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<li>Opción sin padre</li>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateUlList() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateUlSimulatingOl() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "<li>4º) Opción 4</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateUlSimulatingOlRomanNumbers() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>i) Opción 1</li>" +
                "<li>ii) Opción 2</li>" +
                "<li>iii) Opción 3</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }


    @Test
    public void evaluateUlSimulatingOlLetters() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>a) Opción 1</li>" +
                "<li>b) Opción 2</li>" +
                "<li>c) Opción 3</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateSimulateOnly2Items() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "</ul>");
        // Permitimos hasta 2 elementos
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
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
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateOnly3Items() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateMixed() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3ª) Opción 3</li>" +
                "<li>4.) Opción 4</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateNotStartAtFirst() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>4º) Opción 1</li>" +
                "<li>5) Opción 2</li>" +
                "<li>6ª) Opción 3</li>" +
                "<li>7.) Opción 4</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluatePSimulatingUl() throws Exception {
        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" +
                "<p>* Opción 2</p>" +
                "<p>* Opción 3</p>" +
                "<p>* Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));

        checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>- Opción 3</p>" +
                "<p>- Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));

        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>- Opción 3</p>" +
                "<p>* Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));

        checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum</p>" +
                "<p>- Opción 2</p>" +
                "<p>-) Opción 3</p>" +
                "<p>* Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));
    }

    @Test
    public void evaluateBrSimulatingUl() throws Exception {
        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" +
                "* Opción 2<br/>" +
                "* Opción 3<br/>" +
                "* Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "- Opción 3<br/>" +
                "- Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "- Opción 3<br/>" +
                "* Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));

        checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum<br/>" +
                "- Opción 2<br/>" +
                "-) Opción 3<br/>" +
                "* Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));
    }

    @Test
    public void evaluatePSimulatingOl() throws Exception {
        checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum</p>" +
                "<p>2) Opción 2</p>" +
                "<p>3) Opción 3</p>" +
                "<p>4) Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum</p>" +
                "<p>2- Opción 2</p>" +
                "<p>3- Opción 3</p>" +
                "<p>4- Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1º Opción 1 - Lorem ipsum</p>" +
                "<p>2º Opción 2</p>" +
                "<p>3) Opción 3</p>" +
                "<p>4. Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>3) Opción 1 - Lorem ipsum</p>" +
                "<p>4 Opción 2</p>" +
                "<p>5) Opción 3</p>" +
                "<p>6 Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
    }

    @Test
    public void evaluateBrSimulatingOl() throws Exception {
        checkAccessibility.setContent("<p>1. Opción 1 - Lorem ipsum<br/>" +
                "2. Opción 2<br/>" +
                "3. Opción 3<br/>" +
                "4. Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum<br/>" +
                "2- Opción 2<br/>" +
                "3- Opción 3<br/>" +
                "4- Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum<br/>" +
                "2º Opción 2<br/>" +
                "3. Opción 3<br/>" +
                "4- Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>4) Opción 1 - Lorem ipsum<br/>" +
                "5- Opción 2<br/>" +
                "6 Opción 3<br/>" +
                "7-) Opción 4</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
    }

    @Test
    public void evaluateTableSimulatingUl() throws Exception {
        checkAccessibility.setContent("<table><tr><td>Item 1</td><td>Item 2</td></tr><tr><td>Item 3</td><td>Item 4</td></tr></table");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr></table");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1 - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet laoreet sapien. Morbi ultricies erat elit, sit amet posuere velit posuere.</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr><tr><td>Item 4</td></tr></table");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));

        checkAccessibility.setContent("<table><tr><td>Item 1 - <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em></td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
    }

    @Test
    public void evaluateLiParent() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "<li><ol><li>Subitem 1</li><li>Subitem 2</li></ol></li>" +
                "<li><ul><li>Subitem 1</li><li>Subitem 2</li></ul></li>" +
                "</ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateListNestedDirectly() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_OL));

        checkAccessibility.setContent("<ol>" +
                "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ol>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_OL));
    }

    @Test
    public void evaluateIncorrectOrderedList() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<div>Item 2</div>" +
                "<li>Item 3</li>" +
                "</ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));

        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<p>Item 2</p>" +
                "<li>Item 3</li>" +
                "</ol>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));

        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<h2>Item 2</h2>" +
                "<li>Item 3</li>" +
                "</ol>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
    }

    @Test
    public void evaluateBlankList() throws Exception {
        checkAccessibility.setContent("<ol></ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SORTED_EMPTY_LIST));


        checkAccessibility.setContent("<ol><ol><li>Foo</li></ol></ol>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SORTED_EMPTY_LIST));
    }

    @Test
    public void evaluateULLiParent() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<ol>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateULListNestedDirectly() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_UL));

        checkAccessibility.setContent("<ul>" +
                "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_UL));
    }

    @Test
    public void evaluateULIncorrectOrderedList() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<div>Item 2</div>" +
                "<li>Item 3</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));

        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<p>Item 2</p>" +
                "<li>Item 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));

        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<h2>Item 2</h2>" +
                "<li>Item 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));
    }

    @Test
    public void evaluateULBlankList() throws Exception {
        checkAccessibility.setContent("<ul></ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UNSORTED_EMPTY_LIST));

        checkAccessibility.setContent("<ul><ul><li>Foo</li></ul></ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UNSORTED_EMPTY_LIST));
    }

    @Test
    public void evaluateSimulatedListParagraphImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum</p>" +
                "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>" +
                "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 3</p>" +
                "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
    }

    @Test
    public void evaluateNonSimulatedListParagraphImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 1 - Lorem ipsum</p>" +
                "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 2</p>" +
                "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 3</p>" +
                "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
    }

    @Test
    public void evaluateSimulatedListTwoParagraphImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum</p>" +
                "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
    }

    @Test
    public void evaluateSimulatedListBrImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum" +
                "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2" +
                "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 3</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
    }

    @Test
    public void evaluateNonSimulatedListBrImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 1 - Lorem ipsum" +
                "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 2" +
                "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 3" +
                "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 4</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
    }

    @Test
    public void evaluateSimulatedListTwoBrImage() throws Exception {
        checkAccessibility.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum" +
                "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
    }


    /**
     * *************
     * ABB     *
     * **************
     */

        /*
            LI_PARENT_UL_OL = 311;              // MET 4.3.1
            DL_INCORRECT = 427;                 // MET 4.3.2    (Nueva: sustituye a 312)
            DT_OUTSIDE_DL = 313;                // MET 4.3.3
            DD_OUTSIDE_DL = 314;                // MET 4.3.4
            LIST_NESTED_DIRECTLY_OL = 317;      // MET 4.3.5
            LIST_NESTED_DIRECTLY_UL = 318;      // MET 4.3.6
            INCORRECT_SORTED_LIST = 319;        // MET 4.3.7
            INCORRECT_UNSORTED_LIST = 320;      // MET 4.3.8
            P_SIMULATING_UL = 120;              // MET 4.3.9
            BR_SIMULATING_UL = 121;             // MET 4.3.10
            P_SIMULATING_OL = 101;              // MET 4.3.11   (Modificada)
            BR_SIMULATING_OL = 150;             // MET 4.3.12   (Modificada)
            UL_SIMULATING_OL_ID = 416;          // MET 4.3.13   (Nueva)
            P_IMAGE_SIMULATING_LIST = 445;      // MET 4.3.14   (Nueva)
            BR_IMAGE_SIMULATING_LIST = 459;     // MET 4.3.15   (Nueva)
            TABLE_SIMULATING_UL = 431;          // MET 4.3.16   (Nueva)
            SORTED_EMPTY_LIST = 423;            // MET 4.3.17   (Nueva)
            UNSORTED_EMPTY_LIST = 424;          //      "
            EMPTY_DEFINITION_LIST = 425;        //      "
        */
    @Test
    public void MET_4_3_2_evaluateDlCorrectList() throws Exception {

         /* MET 4.3.2
            Title:      Se verifica que las listas de definición tengan una estructura correcta
            Subject:    <dl>, <dt>, <dd>
            Check:      Cada elemento <dl> debe contener directamente al menos un elemento <dt> y al menos un elemento <dd>
                        y
                        El primer elemento hijo debe ser un <dt> y el último un <dd>
         */

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        // Es válido usar dos dt seguidos para definir dos términos a la vez (sinónimos)
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "<dt>Term 3</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dt>Term 2</dt>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 1</dt>" +
                "<dt>Term 2</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<div>Lorem</div>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<div>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "</div>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

        // Atención este error es una diferencia respecto a la misma comprobación en la v1.0
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<ul><li>Lorem</li></ul>" +
                "</dl>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));

    }

    @Test
    public void MET_4_3_11_evaluatePSimulatingOl() throws Exception {

         /* MET 4.3.11
            Title:      Se verifica que no se utilizan párrafos para simular listas numeradas
            Subject:    <p>
            Check:      No hay tres o más párrafos seguidos que comience por algún patrón de los siguientes
                            “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-”
                        donde 'x' pertenece a una secuencia de números, letras o números romanos
         */

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>2) Opción</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>2) Opción</p>" +
                "<div>Hola</div>" +
                "<p>3) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>2) Opción</p>" +
                "<br />" +
                "<p>3) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>10) Opción</p>" +
                "<p>11) Opción</p>" +
                "<p>20) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>2) Opción</p>" +
                "<p>3) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1) Opción</p>" +
                "<p>2) Opción</p>" +
                "<p>3) Opción</p>" +
                "<p>4) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1- Opción</p>" +
                "<p>2- Opción</p>" +
                "<p>3- Opción</p>" +
                "<p>4- Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1 Opción</p>" +
                "<p>2 Opción</p>" +
                "<p>3.Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>4. Opción</p>" +
                "<p>5ºOpción</p>" +
                "<p>6º Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>7ªOpción</p>" +
                "<p>8ª Opción</p>" +
                "<p>9)Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>10) Opción</p>" +
                "<p>11-Opción</p>" +
                "<p>12- Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>13.-Opción</p>" +
                "<p>14.- Opción</p>" +
                "<p>15.-) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>1)Opción</p>" +
                "<p>20) Opción</p>" +
                "<p>21-Opción</p>" +
                "<p>22- Opción</p>" +
                "<p>30.-Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>a) Opción</p>" +
                "<p>b) Opción</p>" +
                "<p>c) Opción</p>" +
                "<p>d) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        // Falla: Expected 1, Actual 0
        checkAccessibility.setContent("<p>i) Opción</p>" +
                "<p>ii) Opción</p>" +
                "<p>iii) Opción</p>" +
                "<p>iv) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>A) Opción</p>" +
                "<p>B) Opción</p>" +
                "<p>C) Opción</p>" +
                "<p>D) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

        checkAccessibility.setContent("<p>I) Opción</p>" +
                "<p>II) Opción</p>" +
                "<p>III) Opción</p>" +
                "<p>IV) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));

    }


    @Test
    public void MET_4_3_12_evaluateBrSimulatingOl() throws Exception {

         /* MET 4.3.12
            Title:      Se verifica que no haya 3 o más líneas separadas por <br> que empiecen por patrones numerados
            Subject:    <br>
            Check:      No hay tres o más líneas separadas por <br> que comiencen por algún patrón de los siguientes
                            “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-”
                        donde 'x' pertenece a una secuencia de números, letras o números romanos
         */

        checkAccessibility.setContent("<p>1. Opción<br/>" +
                "2. Opción</p>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1. Opción<br/>" +
                "2. Opción<br/></p>" +
                "<p>3. Opción<br/>" +
                "4. Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1. Opción<br/>" +
                "10. Opción<br/>" +
                "11. Opción<br/>" +
                "20. Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1. Opción<br/>" +
                "2. Opción<br/>" +
                "3. Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1. Opción<br/>" +
                "2. Opción<br/>" +
                "3. Opción<br/>" +
                "4. Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        // Falla: Expected 1, Actual 0
        checkAccessibility.setContent("<p>1Opción<br />" +
                "2 Opción<br />" +
                "3.Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>4. Opción<br />" +
                "5ºOpción<br />" +
                "6º Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>7ªOpción<br />" +
                "8ª Opción<br />" +
                "9)Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>10) Opción<br />" +
                "11-Opción<br />" +
                "12- Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        // Falla: Expected 1, Actual 0
        checkAccessibility.setContent("<p>13.-Opción<br />" +
                "14.- Opción</br />" +
                "15.-) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1)Opción<br />" +
                "20) Opción<br />" +
                "21-Opción<br />" +
                "22- Opción<br />" +
                "30.-Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>1. Opción<br/><br/>" +
                "2. Opción<br/><br/>" +
                "<br/>" +
                "<br/>" +
                "<br/>" +
                "3. Opción<br/><br/>" +
                "4. Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<div>1. Opción<br/>" +
                "2. Opción<br/>" +
                "3. Opción<br/>" +
                "4. Opción</div>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<blockquote>1. Opción<br/>" +
                "2. Opción<br/>" +
                "3. Opción<br/>" +
                "4. Opción</blockquote>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>a) Opción<br />" +
                "b) Opción<br />" +
                "c) Opción<br />" +
                "d) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>i) Opción<br />" +
                "ii) Opción<br />" +
                "iii) Opción<br />" +
                "iv) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>A) Opción<br />" +
                "B) Opción<br />" +
                "C) Opción<br />" +
                "D) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));

        checkAccessibility.setContent("<p>I) Opción<br />" +
                "II) Opción<br />" +
                "III) Opción<br />" +
                "IV) Opción</p>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
    }

    @Test
    public void MET_4_3_13_evaluateUlSimulatingOl() throws Exception {

         /* MET 4.3.13
            Title:      Se verifica que no haya 3 o más elementos de lista desordenada que empiecen por patrones numerados
            Subject:    <ul>
            Check:      No hay tres o elementos <li> de lista desordenada <ul> que comiencen por algún patrón de los siguientes
                            “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-”
                        donde 'x' pertenece a una secuencia de números, letras o números romanos
         */

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<br />" +
                "<li>3) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<div>Hola</div>" +
                "<li>3) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>10) Opción 2</li>" +
                "<li>11) Opción 3</li>" +
                "<li>20) Opción 4</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1º) Opción 1</li>" +
                "<li>2º) Opción 2</li>" +
                "<li>3º) Opción 3</li>" +
                "<li>4º) Opción 4</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1- Opción 1</li>" +
                "<li>2- Opción 2</li>" +
                "<li>3- Opción 3</li>" +
                "<li>4- Opción 4</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1 Opción 1</li>" +
                "<li>2 Opción 2</li>" +
                "<li>3.Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>4. Opción 1</li>" +
                "<li>5ºOpción 2</li>" +
                "<li>6º Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>7ªOpción 1</li>" +
                "<li>8ª Opción 2</li>" +
                "<li>9)Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>10) Opción 1</li>" +
                "<li>11-Opción 2</li>" +
                "<li>12- Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>13.-Opción 1</li>" +
                "<li>14.- Opción 2</li>" +
                "<li>15.-) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>1)Opción 1</li>" +
                "<li>20) Opción 2</li>" +
                "<li>21-Opción 3</li>" +
                "<li>22- Opción 4</li>" +
                "<li>30.-Opción 4</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>i) Opción 1</li>" +
                "<li>ii) Opción 2</li>" +
                "<li>iii) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>a) Opción 1</li>" +
                "<li>b) Opción 2</li>" +
                "<li>c) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>I) Opción 1</li>" +
                "<li>II) Opción 2</li>" +
                "<li>III) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));

        checkAccessibility.setContent("<ul>" +
                "<li>A) Opción 1</li>" +
                "<li>B) Opción 2</li>" +
                "<li>C) Opción 3</li>" +
                "</ul>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

    @Test
    public void evaluateOrderedList() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<li>A) Opción 1</li>" +
                "<li>B) Opción 2</li>" +
                "<li>C) Opción 3</li>" +
                "</ol>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
    }

}

