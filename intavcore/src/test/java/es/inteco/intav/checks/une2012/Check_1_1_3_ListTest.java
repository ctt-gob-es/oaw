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

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;

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
	public void evaluateDlCorrectList() {
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dd>Definition 2</dd>" + "</dl>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		// Es válido usar dos dt seguidos para definir dos términos a la vez (sinónimos)
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dt>Term 2</dt>" + "<dt>Term 3</dt>" + "<dd>Definition 2</dd>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void evaluateDTConsecutive() {
		checkAccessibility.setContent("<dl>" + "<dd>Definition 1</dd>" + "<dt>Term 2</dt>" + "<dt>Term 3</dt>" + "<dd>Definition 2</dd>" + "</dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void evaluateDTEnding() {
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dt>Term 2</dt>" + "</dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void evaluateDIVInsideDL() throws Exception {
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<div>Lorem</div>" + "</dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void evaluateULInsideDL() {
		// Atención este error es una diferencia respecto a la misma comprobación en la v1.0
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<ul><li>Lorem</li></ul>" + "</dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void evaluateDTOutsideDL() {
		checkAccessibility.setContent("<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dd>Definition 2</dd>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DT_OUTSIDE_DL));
	}

	@Test
	public void evaluateDDOutsideDL() {
		checkAccessibility.setContent("<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dd>Definition 2</dd>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), DD_OUTSIDE_DL));
	}

	@Test
	public void evaluateDLBlankList() {
		checkAccessibility.setContent("<dl></dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EMPTY_DEFINITION_LIST));
	}

	@Test
	public void evaluateDLNestedBlankList() {
		checkAccessibility.setContent("<dl><dl><dt>Foo</dt><dd>Bar</dd></dl></dl>");
		final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EMPTY_DEFINITION_LIST));
	}

	@Test
	public void evaluateLiParentULOL() {
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "</ul>" + "<ol>" + "<li>3) Opción 3</li>" + "<li>4) Opción 4</li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
	}

	@Test
	public void evaluateLiNoParent() {
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "</ul>" + "<li>Opción sin padre</li>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
	}

	@Test
	public void evaluateUlList() {
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "<li>3) Opción 3</li>" + "<li>4) Opción 4</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateUlList2() {
		checkAccessibility.setContent("<ul>" + "<li>Modelo 400</li>" + "<li>Modelo 400-A</li>" + "<li>Modelo 400-B</li>" + "<li>Modelo 400-C</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>Introduccion</li>" + "<li>1. A</li>" + "<li>2) A</li>" + "<li>3- A</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateUlSimulatingOl() {
		checkAccessibility.setContent("<ul>" + "<li>1º) Opción 1</li>" + "<li>2º) Opción 2</li>" + "<li>3º) Opción 3</li>" + "<li>4º) Opción 4</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateUlSimulatingOlRomanNumbers() {
		checkAccessibility.setContent("<ul>" + "<li>i) Opción 1</li>" + "<li>ii) Opción 2</li>" + "<li>iii) Opción 3</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateUlSimulatingOlLetters() {
		checkAccessibility.setContent("<ul>" + "<li>a) Opción 1</li>" + "<li>b) Opción 2</li>" + "<li>c) Opción 3</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateSimulateOnly2Items() {
		checkAccessibility.setContent("<ul>" + "<li>1º) Opción 1</li>" + "<li>2º) Opción 2</li>" + "</ul>");
		// Permitimos hasta 2 elementos
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateNotSortedItems() {
		checkAccessibility.setContent("<ul>" + "<li>5º) Opción 5</li>" + "<li>2º) Opción 2</li>" + "<li>3º) Opción 3</li>" + "<li>1º) Opción 1</li>" + "<li>4º) Opción 4</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateOnly3Items() {
		checkAccessibility.setContent("<ul>" + "<li>1º) Opción 1</li>" + "<li>2º) Opción 2</li>" + "<li>3º) Opción 3</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateMixed() {
		checkAccessibility.setContent("<ul>" + "<li>1º) Opción 1</li>" + "<li>2) Opción 2</li>" + "<li>3ª) Opción 3</li>" + "<li>4.) Opción 4</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateNotStartAtFirst() {
		checkAccessibility.setContent("<ul>" + "<li>4º) Opción 1</li>" + "<li>5) Opción 2</li>" + "<li>6ª) Opción 3</li>" + "<li>7.) Opción 4</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluatePSimulatingUl() {
		checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" + "<p>* Opción 2</p>" + "<p>* Opción 3</p>" + "<p>* Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));
		checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum</p>" + "<p>- Opción 2</p>" + "<p>- Opción 3</p>" + "<p>- Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));
		checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum</p>" + "<p>- Opción 2</p>" + "<p>- Opción 3</p>" + "<p>* Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));
		checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum</p>" + "<p>- Opción 2</p>" + "<p>-) Opción 3</p>" + "<p>* Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_UL));
	}

	@Test
	public void evaluateBrSimulatingUl() {
		checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" + "* Opción 2<br/>" + "* Opción 3<br/>" + "* Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));
		checkAccessibility.setContent("<p>- Opción 1 - Lorem ipsum<br/>" + "- Opción 2<br/>" + "- Opción 3<br/>" + "- Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));
		checkAccessibility.setContent("<p>* Opción 1 - Lorem ipsum<br/>" + "- Opción 2<br/>" + "- Opción 3<br/>" + "* Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));
		checkAccessibility.setContent("<p>*) Opción 1 - Lorem ipsum<br/>" + "- Opción 2<br/>" + "-) Opción 3<br/>" + "* Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_UL));
	}

	@Test
	public void evaluatePSimulatingOl() {
		checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum</p>" + "<p>2) Opción 2</p>" + "<p>3) Opción 3</p>" + "<p>4) Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum</p>" + "<p>2- Opción 2</p>" + "<p>3- Opción 3</p>" + "<p>4- Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1º Opción 1 - Lorem ipsum</p>" + "<p>2º Opción 2</p>" + "<p>3) Opción 3</p>" + "<p>4. Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>3) Opción 1 - Lorem ipsum</p>" + "<p>4 Opción 2</p>" + "<p>5) Opción 3</p>" + "<p>6 Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
	}

	@Test
	public void evaluateBrSimulatingOl() {
		checkAccessibility.setContent("<p>1. Opción 1 - Lorem ipsum<br/>" + "2. Opción 2<br/>" + "3. Opción 3<br/>" + "4. Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1- Opción 1 - Lorem ipsum<br/>" + "2- Opción 2<br/>" + "3- Opción 3<br/>" + "4- Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción 1 - Lorem ipsum<br/>" + "2º Opción 2<br/>" + "3. Opción 3<br/>" + "4- Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>4) Opción 1 - Lorem ipsum<br/>" + "5- Opción 2<br/>" + "6 Opción 3<br/>" + "7-) Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		// Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		// Debido a cambios en el patrón, sólo las listas que empiecen en 1 serán inválidas
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility
				.setContent("<blockquote>\n" + "<p>a)&nbsp;La declaración de inadmisibilidad de la petición.<br>\n" + "b)&nbsp;La omisión de la obligación de contestar en el plazo establecido.<br>\n"
						+ "c)&nbsp;La ausencia en la contestación de los requisitos mínimos establecidos en el artículo 11 de la Ley Orgánica 4/2001.</p>\n" + "</blockquote>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>2. Elemento 1<br/>" + "3. Elemento 2<br/>" + "4. Elemento 3<br/>" + "5- Elemento 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>5. Opción 1 - Lorem ipsum<br/>" + "6. Opción 2<br/>" + "7. Opción 3<br/>" + "8. Opción 4</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
	}

	@Test
	public void evaluateTableSimulatingUl() {
		checkAccessibility.setContent("<table><tr><td>Item 1</td><td>Item 2</td></tr><tr><td>Item 3</td><td>Item 4</td></tr></table");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
		checkAccessibility.setContent("<table><tr><td>Item 1</td></tr></table>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
		checkAccessibility.setContent(
				"<table><tr><td>Item 1 - Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet laoreet sapien. Morbi ultricies erat elit, sit amet posuere velit posuere.</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
		checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
		checkAccessibility.setContent("<table><tr><td>Item 1</td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr><tr><td>Item 4</td></tr></table>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
		checkAccessibility.setContent(
				"<table><tr><td>Item 1 - <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em> <strong><em>Lorem ipsum</strong></em></td></tr><tr><td>Item 2</td></tr><tr><td>Item 3</td></tr></table>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABLE_SIMULATING_UL));
	}

	@Test
	public void evaluateLiParent() {
		checkAccessibility.setContent(
				"<ol>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "<li><ol><li>Subitem 1</li><li>Subitem 2</li></ol></li>" + "<li><ul><li>Subitem 1</li><li>Subitem 2</li></ul></li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
	}

	@Test
	public void evaluateListNestedDirectly() {
		checkAccessibility.setContent("<ol>" + "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_OL));
		checkAccessibility.setContent("<ol>" + "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "</ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_OL));
	}

	@Test
	public void evaluateIncorrectOrderedList() {
		checkAccessibility.setContent("<ol>" + "<li>Item 1</li>" + "<div>Item 2</div>" + "<li>Item 3</li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
		checkAccessibility.setContent("<ol>" + "<li>Item 1</li>" + "<p>Item 2</p>" + "<li>Item 3</li>" + "</ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
		checkAccessibility.setContent("<ol>" + "<li>Item 1</li>" + "<h2>Item 2</h2>" + "<li>Item 3</li>" + "</ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
		// Según HTML5 las listas admiten etiquetas scripting (script y template)
		checkAccessibility.setContent(
				"<ol>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "<li>Item 3</li>" + "<script type=\"text/javascript\"> aux = 0</script>" + "<template><strong>foo</strong></template>" + "</ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
		checkAccessibility.setContent("<ol>" + "<li>Item 1</li>" + "<ol></li>Item 2</li></ol>" + "<li>Item 3</li>" + "</ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_SORTED_LIST));
	}

	@Test
	public void evaluateBlankList() {
		checkAccessibility.setContent("<ol></ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SORTED_EMPTY_LIST));
		checkAccessibility.setContent("<ol><ol><li>Foo</li></ol></ol>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SORTED_EMPTY_LIST));
	}

	@Test
	public void evaluateULLiParent() {
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "</ul>" + "<ol>" + "<li>3) Opción 3</li>" + "<li>4) Opción 4</li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
	}

	@Test
	public void evaluateULListNestedDirectly() {
		checkAccessibility.setContent("<ul>" + "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_UL));
		checkAccessibility.setContent("<ul>" + "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LIST_NESTED_DIRECTLY_UL));
	}

	@Test
	public void evaluateIncorrectUnorderedList() {
		checkAccessibility.setContent("<ul>" + "<li>Item 1</li>" + "<div>Item 2</div>" + "<li>Item 3</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));
		checkAccessibility.setContent("<ul>" + "<li>Item 1</li>" + "<p>Item 2</p>" + "<li>Item 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));
		checkAccessibility.setContent("<ul>" + "<li>Item 1</li>" + "<h2>Item 2</h2>" + "<li>Item 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));
		// Según HTML5 las listas admiten etiquetas scripting (script y template)
		checkAccessibility.setContent(
				"<ul>" + "<li>Item 1</li>" + "<li>Item 2</li>" + "<li>Item 3</li>" + "<script type=\"text/javascript\"> aux = 0</script>" + "<template><strong>foo</strong></template>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), INCORRECT_UNSORTED_LIST));
	}

	@Test
	public void evaluateULBlankList() {
		checkAccessibility.setContent("<ul></ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UNSORTED_EMPTY_LIST));
		checkAccessibility.setContent("<ul><ul><li>Foo</li></ul></ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UNSORTED_EMPTY_LIST));
	}

	@Test
	public void evaluateSimulatedListParagraphImage() {
		checkAccessibility
				.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum</p>" + "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>"
						+ "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 3</p>" + "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
	}

	@Test
	public void evaluateNonSimulatedListParagraphImage() {
		checkAccessibility
				.setContent("<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 1 - Lorem ipsum</p>" + "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 2</p>"
						+ "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 3</p>" + "<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
	}

	@Test
	public void evaluateSimulatedListTwoParagraphImage() {
		checkAccessibility
				.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum</p>" + "<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_IMAGE_SIMULATING_LIST));
	}

	@Test
	public void evaluateSimulatedListBrImage() {
		checkAccessibility.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum" + "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2"
				+ "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 3</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
	}

	@Test
	public void evaluateNonSimulatedListBrImage() {
		checkAccessibility.setContent("<p><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 1 - Lorem ipsum" + "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 2"
				+ "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 3" + "<br/><img src=\"list.png\" width=\"64\" height=\"64\" alt=\"\">Opción 4</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
	}

	@Test
	public void evaluateSimulatedListTwoBrImage() {
		checkAccessibility
				.setContent("<p><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 1 - Lorem ipsum" + "<br/><img src=\"list.png\" width=\"8\" height=\"8\" alt=\"\">Opción 2</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_IMAGE_SIMULATING_LIST));
	}

	/**
	 * ************* ABB * **************
	 */
	/*
	 * LI_PARENT_UL_OL = 311; // MET 4.3.1 DL_INCORRECT = 427; // MET 4.3.2 (Nueva: sustituye a 312) DT_OUTSIDE_DL = 313; // MET 4.3.3 DD_OUTSIDE_DL = 314; // MET 4.3.4 LIST_NESTED_DIRECTLY_OL = 317;
	 * // MET 4.3.5 LIST_NESTED_DIRECTLY_UL = 318; // MET 4.3.6 INCORRECT_SORTED_LIST = 319; // MET 4.3.7 INCORRECT_UNSORTED_LIST = 320; // MET 4.3.8 P_SIMULATING_UL = 120; // MET 4.3.9
	 * BR_SIMULATING_UL = 121; // MET 4.3.10 P_SIMULATING_OL = 101; // MET 4.3.11 (Modificada) BR_SIMULATING_OL = 150; // MET 4.3.12 (Modificada) UL_SIMULATING_OL_ID = 416; // MET 4.3.13 (Nueva)
	 * P_IMAGE_SIMULATING_LIST = 445; // MET 4.3.14 (Nueva) BR_IMAGE_SIMULATING_LIST = 459; // MET 4.3.15 (Nueva) TABLE_SIMULATING_UL = 431; // MET 4.3.16 (Nueva) SORTED_EMPTY_LIST = 423; // MET
	 * 4.3.17 (Nueva) UNSORTED_EMPTY_LIST = 424; // " EMPTY_DEFINITION_LIST = 425; // "
	 */
	@Test
	public void metEvaluateDlCorrectList() {
		/*
		 * MET 4.3.2 Title: Se verifica que las listas de definición tengan una estructura correcta Subject: <dl>, <dt>, <dd> Check: Cada elemento <dl> debe contener directamente al menos un elemento
		 * <dt> y al menos un elemento <dd> y El primer elemento hijo debe ser un <dt> y el último un <dd>
		 */
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dd>Definition 2</dd>" + "</dl>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		// Es válido usar dos dt seguidos para definir dos términos a la vez (sinónimos)
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dt>Term 2</dt>" + "<dt>Term 3</dt>" + "<dd>Definition 2</dd>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dt>Term 2</dt>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<dd>Definition 1</dd>" + "<dd>Definition 2</dd>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<dd>Definition 1</dd>" + "<dt>Term 1</dt>" + "<dt>Term 2</dt>" + "<dd>Definition 2</dd>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<dt>Term 2</dt>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<div>Lorem</div>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		checkAccessibility.setContent("<dl>" + "<div>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "</div>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
		// Atención este error es una diferencia respecto a la misma comprobación en la v1.0
		checkAccessibility.setContent("<dl>" + "<dt>Term 1</dt>" + "<dd>Definition 1</dd>" + "<ul><li>Lorem</li></ul>" + "</dl>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), DL_INCORRECT));
	}

	@Test
	public void metEvaluatePSimulatingOl() {
		/*
		 * MET 4.3.11 Title: Se verifica que no se utilizan párrafos para simular listas numeradas Subject: <p> Check: No hay tres o más párrafos seguidos que comience por algún patrón de los
		 * siguientes “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-” donde 'x' pertenece a una secuencia de números, letras o números romanos
		 */
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>2) Opción</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>2) Opción</p>" + "<div>Hola</div>" + "<p>3) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>2) Opción</p>" + "<br />" + "<p>3) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>10) Opción</p>" + "<p>11) Opción</p>" + "<p>20) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>2) Opción</p>" + "<p>3) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1) Opción</p>" + "<p>2) Opción</p>" + "<p>3) Opción</p>" + "<p>4) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1- Opción</p>" + "<p>2- Opción</p>" + "<p>3- Opción</p>" + "<p>4- Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1 Opción</p>" + "<p>2 Opción</p>" + "<p>3.Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>4. Opción</p>" + "<p>5ºOpción</p>" + "<p>6º Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>7ªOpción</p>" + "<p>8ª Opción</p>" + "<p>9)Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>10) Opción</p>" + "<p>11-Opción</p>" + "<p>12- Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>13.-Opción</p>" + "<p>14.- Opción</p>" + "<p>15.-) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>1)Opción</p>" + "<p>20) Opción</p>" + "<p>21-Opción</p>" + "<p>22- Opción</p>" + "<p>30.-Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>a) Opción</p>" + "<p>b) Opción</p>" + "<p>c) Opción</p>" + "<p>d) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		// Falla: Expected 1, Actual 0
		checkAccessibility.setContent("<p>i) Opción</p>" + "<p>ii) Opción</p>" + "<p>iii) Opción</p>" + "<p>iv) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>A) Opción</p>" + "<p>B) Opción</p>" + "<p>C) Opción</p>" + "<p>D) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
		checkAccessibility.setContent("<p>I) Opción</p>" + "<p>II) Opción</p>" + "<p>III) Opción</p>" + "<p>IV) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), P_SIMULATING_OL));
	}

	@Test
	public void metEvaluateBrSimulatingOl() {
		/*
		 * MET 4.3.12 Title: Se verifica que no haya 3 o más líneas separadas por <br> que empiecen por patrones numerados Subject: <br> Check: No hay tres o más líneas separadas por <br> que
		 * comiencen por algún patrón de los siguientes “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-” donde 'x' pertenece a una secuencia de números, letras o números romanos
		 */
		checkAccessibility.setContent("<p>1. Opción<br/>" + "2. Opción</p>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1. Opción<br/>" + "2. Opción<br/></p>" + "<p>3. Opción<br/>" + "4. Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1. Opción<br/>" + "10. Opción<br/>" + "11. Opción<br/>" + "20. Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1. Opción<br/>" + "2. Opción<br/>" + "3. Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1. Opción<br/>" + "2. Opción<br/>" + "3. Opción<br/>" + "4. Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		// Falla: Expected 1, Actual 0
		checkAccessibility.setContent("<p>1Opción<br />" + "2 Opción<br />" + "3.Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>4. Opción<br />" + "5ºOpción<br />" + "6º Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>7ªOpción<br />" + "8ª Opción<br />" + "9)Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>10) Opción<br />" + "11-Opción<br />" + "12- Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		// Falla: Expected 1, Actual 0
		checkAccessibility.setContent("<p>13.-Opción<br />" + "14.- Opción</br />" + "15.-) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1)Opción<br />" + "20) Opción<br />" + "21-Opción<br />" + "22- Opción<br />" + "30.-Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>1. Opción<br/><br/>" + "2. Opción<br/><br/>" + "<br/>" + "<br/>" + "<br/>" + "3. Opción<br/><br/>" + "4. Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<div>1. Opción<br/>" + "2. Opción<br/>" + "3. Opción<br/>" + "4. Opción</div>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<blockquote>1. Opción<br/>" + "2. Opción<br/>" + "3. Opción<br/>" + "4. Opción</blockquote>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>a) Opción<br />" + "b) Opción<br />" + "c) Opción<br />" + "d) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>i) Opción<br />" + "ii) Opción<br />" + "iii) Opción<br />" + "iv) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>A) Opción<br />" + "B) Opción<br />" + "C) Opción<br />" + "D) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
		checkAccessibility.setContent("<p>I) Opción<br />" + "II) Opción<br />" + "III) Opción<br />" + "IV) Opción</p>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BR_SIMULATING_OL));
	}

	@Test
	public void metEvaluateUlSimulatingOl() {
		/*
		 * MET 4.3.13 Title: Se verifica que no haya 3 o más elementos de lista desordenada que empiecen por patrones numerados Subject: <ul> Check: No hay tres o elementos <li> de lista desordenada
		 * <ul> que comiencen por algún patrón de los siguientes “x“, “x “, “x.“, “xº”, “xª”, ”x)”, “x-”, “x.-” donde 'x' pertenece a una secuencia de números, letras o números romanos
		 */
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "</ul>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "<br />" + "<li>3) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "<div>Hola</div>" + "<li>3) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>10) Opción 2</li>" + "<li>11) Opción 3</li>" + "<li>20) Opción 4</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "<li>3) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1) Opción 1</li>" + "<li>2) Opción 2</li>" + "<li>3) Opción 3</li>" + "<li>4) Opción 4</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1º) Opción 1</li>" + "<li>2º) Opción 2</li>" + "<li>3º) Opción 3</li>" + "<li>4º) Opción 4</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1- Opción 1</li>" + "<li>2- Opción 2</li>" + "<li>3- Opción 3</li>" + "<li>4- Opción 4</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1 Opción 1</li>" + "<li>2 Opción 2</li>" + "<li>3.Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>4. Opción 1</li>" + "<li>5ºOpción 2</li>" + "<li>6º Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>7ªOpción 1</li>" + "<li>8ª Opción 2</li>" + "<li>9)Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>10) Opción 1</li>" + "<li>11-Opción 2</li>" + "<li>12- Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>13.-Opción 1</li>" + "<li>14.- Opción 2</li>" + "<li>15.-) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>1)Opción 1</li>" + "<li>20) Opción 2</li>" + "<li>21-Opción 3</li>" + "<li>22- Opción 4</li>" + "<li>30.-Opción 4</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>i) Opción 1</li>" + "<li>ii) Opción 2</li>" + "<li>iii) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>a) Opción 1</li>" + "<li>b) Opción 2</li>" + "<li>c) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>I) Opción 1</li>" + "<li>II) Opción 2</li>" + "<li>III) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
		checkAccessibility.setContent("<ul>" + "<li>A) Opción 1</li>" + "<li>B) Opción 2</li>" + "<li>C) Opción 3</li>" + "</ul>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateOrderedList() {
		checkAccessibility.setContent("<ol>" + "<li>A) Opción 1</li>" + "<li>B) Opción 2</li>" + "<li>C) Opción 3</li>" + "</ol>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), UL_SIMULATING_OL_ID));
	}

	@Test
	public void evaluateListOfTables() {
		// Codigo extraido de: http://www.minhap.gob.es/es-ES/CDI/Paginas/InformacionPresupuestaria/InformacionPresupuestaria.aspx
		final String content = "<h2 class=\"titulo\">Información Presupuestaria </h2>\n" + "            <div id=\"contSubCDI\">\n" + "                <ul class=\"listadoCanalesSubCanales\">\n"
				+ "                    <li>\n" + "                        <div class=\"expand_capa\" id=\"tabla113\">\n"
				+ "                            <div class=\"cabCeldaSubcanales cabSinJs\" style=\"height: 55px;\"><p><span\n" + "                                    class=\"imgSubcanales\"><img\n"
				+ "                                    src=\"/Style%20Library/MINHAC.SP.Portal/img/ico-ArrowCircle.png\"\n"
				+ "                                    alt=\"Desplegar\"></span><span class=\"enlacesSubcanales\">Información Administración Central</span>\n"
				+ "                            </p></div>\n" + "                        </div>\n" + "                            <div class=\"bloqueCont\">\n"
				+ "                                        <table class=\"tabla cellpadding5 tablaestadisticas breakcadena colum7\"\n"
				+ "                                               id=\"tabla113\">\n"
				+ "                                            <caption class=\"oculto\">Tabla con información acerca de Información\n"
				+ "                                                Administración Central\n" + "                                            </caption>\n"
				+ "                                            <colgroup></colgroup>\n" + "                                            <colgroup>\n"
				+ "                                                <col>\n" + "                                                <col>\n" + "                                                <col>\n"
				+ "                                                <col>\n" + "                                                <col>\n" + "                                                <col>\n"
				+ "                                            </colgroup>\n" + "                                            <thead>\n"
				+ "                                            <tr class=\"tabla_titulo2 bordesup encabezadotabla\">\n" + "                                                <th class=\"celldatos\"\n"
				+ "                                                    style=\"&#xD;&#xA;                        word-wrap: normal;&#xD;&#xA;                      \">\n"
				+ "                                                    Información Administración Central\n" + "                                                </th>\n"
				+ "                                                <th id=\"tabla113th_author\">Autor</th>\n"
				+ "                                                <th id=\"tabla113th_periodicity\">Periodicidad</th>\n"
				+ "                                                <th id=\"tabla113th_lastData\">Últimos datos</th>\n"
				+ "                                                <th id=\"tabla113th_nextUpdate\">Próxima actualización</th>\n"
				+ "                                                <th id=\"tabla113th_support\">Soporte</th>\n"
				+ "                                                <th class=\"celldatos2\" id=\"tabla113th_moreInfo\">Más Información</th>\n" + "                                            </tr>\n"
				+ "                                            </thead>\n" + "                                            <tbody>\n"
				+ "                                            <tr class=\"bordesup\">\n" + "                                                <td class=\"cuadro subcabecera\" id=\"tabla113257\"\n"
				+ "                                                    style=\"&#xD;&#xA;          word-wrap: normal;&#xD;&#xA;        \">\n"
				+ "                                                    <div class=\"divdatos\"><p><a\n"
				+ "                                                            title=\"Abre nueva ventana: Presupuestos Generales del Estado del año en curso\"\n"
				+ "                                                            target=\"_blank\"\n"
				+ "                                                            href=\"http://www.sepg.pap.minhap.gob.es/sitios/sepg/es-ES/Presupuestos/pge2015/Paginas/pge2015.aspx\">Presupuestos\n"
				+ "                                                        Generales del Estado del año en curso<img\n"
				+ "                                                                class=\"Technositepopup\" alt=\"Abre nueva ventana\"\n"
				+ "                                                                src=\"/Style%20Library/MINHAC.SP.Portal/img/popup.gif\"></a>\n"
				+ "                                                    </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_author\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_author\">\n" + "                                                    <div><p>DGP </p></div>\n"
				+ "                                                </td>\n" + "                                                <td id=\"tabla113257 tabla113th_periodicity\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_periodicity\">\n"
				+ "                                                    <div><p>Anual </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_lastData\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_lastData\">\n"
				+ "                                                    <div><p>Año 2016 </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_nextUpdate\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_nextUpdate\">\n"
				+ "                                                    <div><p>30/12/2016</p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_support\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_support\">\n"
				+ "                                                    <div><p>html, pdf </p></div>\n" + "                                                </td>\n"
				+ "                                                <td class=\"celldatos2\">\n" + "                                                    <ul>\n"
				+ "                                                        <li>\n"
				+ "                                                            <a href=\"http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/Notas%20Prensa/2015/S.E.%20PRESUPUESTOS%20Y%20GASTOS/20-10-15%20NP%20Aprobación%20PGE%202016.pdf\"\n"
				+ "                                                               target=\"_blank\">Nota de prensa</a></li>\n" + "                                                        <li>\n"
				+ "                                                            <a href=\"http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/Notas%20Prensa/2015/S.E.%20PRESUPUESTOS%20Y%20GASTOS/04-08-15%20Presentaci%c3%b3n%20PGE%202016.pdf\"\n"
				+ "                                                               target=\"_blank\">Presupuestos Generales del Estado\n"
				+ "                                                                2016</a></li>\n" + "                                                    </ul>\n"
				+ "                                                </td>\n" + "                                            </tr>\n" + "                                       </table>\n"
				+ "                                   </div>" + "                               </li>" + "                               <li>"
				+ "                               <div class=\"expand_capa\" id=\"tabla113\">"
				+ "                            <div class=\"cabCeldaSubcanales cabSinJs\" style=\"height: 55px;\"><p><span\n" + "                                    class=\"imgSubcanales\"><img\n"
				+ "                                    src=\"/Style%20Library/MINHAC.SP.Portal/img/ico-ArrowCircle.png\"\n"
				+ "                                    alt=\"Desplegar\"></span><span class=\"enlacesSubcanales\">Información Administración Central</span>\n"
				+ "                            </p></div>\n" + "                        </div>\n" + "                            <div class=\"bloqueCont\">\n"
				+ "                                        <table class=\"tabla cellpadding5 tablaestadisticas breakcadena colum7\"\n"
				+ "                                               id=\"tabla113\">\n"
				+ "                                            <caption class=\"oculto\">Tabla con información acerca de Información\n"
				+ "                                                Administración Central\n" + "                                            </caption>\n"
				+ "                                            <colgroup></colgroup>\n" + "                                            <colgroup>\n"
				+ "                                                <col>\n" + "                                                <col>\n" + "                                                <col>\n"
				+ "                                                <col>\n" + "                                                <col>\n" + "                                                <col>\n"
				+ "                                            </colgroup>\n" + "                                            <thead>\n"
				+ "                                            <tr class=\"tabla_titulo2 bordesup encabezadotabla\">\n" + "                                                <th class=\"celldatos\"\n"
				+ "                                                    style=\"&#xD;&#xA;                        word-wrap: normal;&#xD;&#xA;                      \">\n"
				+ "                                                    Información Administración Central\n" + "                                                </th>\n"
				+ "                                                <th id=\"tabla113th_author\">Autor</th>\n"
				+ "                                                <th id=\"tabla113th_periodicity\">Periodicidad</th>\n"
				+ "                                                <th id=\"tabla113th_lastData\">Últimos datos</th>\n"
				+ "                                                <th id=\"tabla113th_nextUpdate\">Próxima actualización</th>\n"
				+ "                                                <th id=\"tabla113th_support\">Soporte</th>\n"
				+ "                                                <th class=\"celldatos2\" id=\"tabla113th_moreInfo\">Más Información</th>\n" + "                                            </tr>\n"
				+ "                                            </thead>\n" + "                                            <tbody>\n"
				+ "                                            <tr class=\"bordesup\">\n" + "                                                <td class=\"cuadro subcabecera\" id=\"tabla113257\"\n"
				+ "                                                    style=\"&#xD;&#xA;          word-wrap: normal;&#xD;&#xA;        \">\n"
				+ "                                                    <div class=\"divdatos\"><p><a\n"
				+ "                                                            title=\"Abre nueva ventana: Presupuestos Generales del Estado del año en curso\"\n"
				+ "                                                            target=\"_blank\"\n"
				+ "                                                            href=\"http://www.sepg.pap.minhap.gob.es/sitios/sepg/es-ES/Presupuestos/pge2015/Paginas/pge2015.aspx\">Presupuestos\n"
				+ "                                                        Generales del Estado del año en curso<img\n"
				+ "                                                                class=\"Technositepopup\" alt=\"Abre nueva ventana\"\n"
				+ "                                                                src=\"/Style%20Library/MINHAC.SP.Portal/img/popup.gif\"></a>\n"
				+ "                                                    </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_author\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_author\">\n" + "                                                    <div><p>DGP </p></div>\n"
				+ "                                                </td>\n" + "                                                <td id=\"tabla113257 tabla113th_periodicity\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_periodicity\">\n"
				+ "                                                    <div><p>Anual </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_lastData\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_lastData\">\n"
				+ "                                                    <div><p>Año 2016 </p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_nextUpdate\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_nextUpdate\">\n"
				+ "                                                    <div><p>30/12/2016</p></div>\n" + "                                                </td>\n"
				+ "                                                <td id=\"tabla113257 tabla113th_support\"\n"
				+ "                                                    headers=\"tabla113257 tabla113th_support\">\n"
				+ "                                                    <div><p>html, pdf </p></div>\n" + "                                                </td>\n"
				+ "                                                <td class=\"celldatos2\">\n" + "                                                    <ul>\n"
				+ "                                                        <li>\n"
				+ "                                                            <a href=\"http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/Notas%20Prensa/2015/S.E.%20PRESUPUESTOS%20Y%20GASTOS/20-10-15%20NP%20Aprobación%20PGE%202016.pdf\"\n"
				+ "                                                               target=\"_blank\">Nota de prensa</a></li>\n" + "                                                        <li>\n"
				+ "                                                            <a href=\"http://www.minhap.gob.es/Documentacion/Publico/GabineteMinistro/Notas%20Prensa/2015/S.E.%20PRESUPUESTOS%20Y%20GASTOS/04-08-15%20Presentaci%c3%b3n%20PGE%202016.pdf\"\n"
				+ "                                                               target=\"_blank\">Presupuestos Generales del Estado\n"
				+ "                                                                2016</a></li>\n" + "                                                    </ul>\n"
				+ "                                                </td>\n" + "                                            </tr>\n" + "                                       </table>\n"
				+ "                               </li>" + "                           </ul>" + "                       </div>";
		checkAccessibility.setContent(content);
		checkAccessibility.setUrl("http://www.minhap.gob.es/es-ES/CDI/Paginas/InformacionPresupuestaria/InformacionPresupuestaria.aspx");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}
}