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
 * The Class Check_1_2_3_AccesibilitySection.
 */
public final class Check_1_2_3_AccesibilitySection {
	/** The Constant MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3. */
	private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3 = "minhap.observatory.2_0.subgroup.1.2.3";
	/** The Constant HAS_NOT_SECTION_LINK. */
	private static final int HAS_NOT_SECTION_LINK = 126;
	/** The Constant ACCESSIBILITY_DECLARATION_NOT_CONTACT. */
	private static final int ACCESSIBILITY_DECLARATION_NOT_CONTACT = 148;
	/** The Constant ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE. */
	private static final int ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE = 149;
	/** The Constant ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL. */
	private static final int ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL = 463;
	/** The check accessibility. */
	private CheckAccessibility checkAccessibility;

	/**
	 * Inits the.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void init() throws Exception {
		EvaluatorUtility.initialize();
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012-b");
	}

	/**
	 * Test no section link.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testNoSectionLink() throws Exception {
		checkAccessibility.setContent("<html><head><title>Foo</title></head><body><p>Lorem ipsum</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_RED_ZERO);
	}

	/**
	 * Test accesibility page no contact.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageNoContact() throws Exception {
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Nivel de conformidad AA (doble A)</p><p>Fecha última revisión 01/01/2015</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
	}

	/**
	 * Test accesibility page no date.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageNoDate() throws Exception {
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Nivel de conformidad AA (doble A)</p><p><a>Contactar</a></p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
	}

	/**
	 * Test accesibility page no conformance.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageNoConformance() throws Exception {
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Lorem ipsum</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
	}

	/**
	 * Test accesibility conformance formats.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityConformanceFormats() throws Exception {
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel de conformidad AA (doble A)</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Prioridad 2</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel doble-a</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivel AA</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>Nivell AA</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>nivell de conformitat ‘Doble-A’ (AA)</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>nivell de conformitat (Doble-A)</p><p>Fecha última revisión 21/1/2015</p><p><a>Contactar</a></p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility contact link.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityContactLink() throws Exception {
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p><a>Contacte con nosotros</a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p><a>contacte con nosotros. </a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p><a>buzón de contacto</a></p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page english date.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageEnglishDate() throws Exception {
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01/24/2015</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 01-24-2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 1.24.2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page galego.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageGalego() throws Exception {
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel alcalzado: AA (doble A)  \n"
				+ "<p>Data da última revisión: 1 de decembro de 2016</p><p class=\"btn_links\">\n" + "<a href=\"/gl/concello\">\n" + "<em class=\"fa fa-lg fa-send\">  </em>\n"
				+ "<span>Contacta</span>\n" + "</a>\n" + "</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility date.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityDate() throws Exception {
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7 de     Octubre de 2015</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7/9/2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7-9-2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 7.9.2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07/09/2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07-09-2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: 07.09.2015</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: Març 7, 2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test special entities.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSpecialEntities() throws Exception {
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p>Fecha última revisión: 23 de noviembre de &nbsp;2016</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p>Fecha última revisión:  23 de noviembre  del &nbsp;2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA&nbsp;(doble A)</p>Fecha última revisión: 23 de noviembre  del &#160;2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA&nbsp;(doble A)</p>Fecha última revisión: 23 de&ensp;noviembre  del &#160;2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: Mar&ccedil; 7, 2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Revisado: Mar&#231; 7, 2016</p></body></html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page year date.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageYearDate() throws Exception {
		checkAccessibility
				.setContent("<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 2015/12/31</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page year date long format.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageYearDateLongFormat() throws Exception {
		checkAccessibility.setContent(
				"<html><head><title>Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Date of last review (14th February 2016)</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility patterns page.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPatternsPage() throws Exception {
		checkAccessibility.setContent(
				"<html><head><title>Portal title - subtitle - Accesibilidad</title></head><body><p>contact@example.com</p><p>Nivel de conformidad AA (doble A)</p><p>Last reviewed: 2015/12/31</p></body></html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test URL.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testURL() throws Exception {
		checkAccessibility.setUrl("http://www.agendadigital.gob.es");
		Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
		checkAccessibility.setUrl("http://www.thespanisheconomy.com/portal/site/tse/menuitem.a2f626f6152b3957b88f9b10026041a0/?vgnextoid=5080a57283d7f310VgnVCM1000002006140aRCRD");
		evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page not linked.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageNotLinked() throws Exception {
		checkAccessibility.setUrl("http://www.agendadigital.gob.es/Paginas/accesibilidad.aspx");
		Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Test accesibility page.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPage() throws Exception {
		// Esta dando problemas posbilemente porque hay más enlaces que
		// detecta como accesibilidad (documentos pdf).
		// Comprobar que no se están mezclando resultados
		checkAccessibility.setUrl("http://www.minhafp.gob.es/es-es/El%20Ministerio/Informacion%20y%20registros/Paginas/Informacion%20Administrativa.aspx");
		// La home del portal se analiza correctamente (solo detecta el enlace a
		// la sección accesibilidad)
		// checkAccessibility.setUrl("http://www.minhafp.gob.es/es-ES/");
		Evaluation evaluation = EvaluatorUtils.evaluate(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_NOT_SECTION_LINK));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_REVISION_DATE));
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONFORMANCE_LEVEL));
		TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
	}

	/**
	 * Formulario de contacto en la propia página.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAccesibilityPageContactForm() throws Exception {
		checkAccessibility.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n" + "        <p>Lorem ipsum</p>\n"
				+ "        <p>Nivel de conformidad AA (doble A)</p>\n" + "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<label for='nombre'>Nombre</label>\n"
				+ "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n" + "			\n" + "			<label for='contacto'>Forma de contacto</label>\n"
				+ "			<input type='text' id='contacto' name='contacto' placeholder='Forma de contacto...'>\n" + "			\n" + "			\n" + "			<input type='submit' value='Enviar'>\n"
				+ "		</form>\n" + "    </body>\n" + "</html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		checkAccessibility.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n" + "        <p>Lorem ipsum</p>\n"
				+ "        <p>Nivel de conformidad AA (doble A)</p>\n" + "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<label for='nombre'>Nombre</label>\n"
				+ "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n" + "			\n" + "			<label for='mensaje'>Mensaje</label>\n"
				+ "			<input type='text' id='mensaje' name='mensaje' placeholder='Mensaje...'>\n" + "			\n" + "			\n" + "			<input type='submit' value='Contacto'>\n"
				+ "		</form>\n" + "    </body>\n" + "</html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		checkAccessibility.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n" + "        <p>Lorem ipsum</p>\n"
				+ "        <p>Nivel de conformidad AA (doble A)</p>\n" + "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<label for='nombre'>Nombre</label>\n"
				+ "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n" + "			\n" + "			<label for='mensaje'>Mensaje</label>\n"
				+ "			<input type='text' id='mensaje' name='mensaje' placeholder='Mensaje...'>\n" + "			\n" + "			\n" + "			<input type='submit' value='Enviar'>\n"
				+ "		</form>\n" + "    </body>\n" + "</html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
	}

	/**
	 * Test accessibilty contact form fieldset.
	 */
	@Test
	public void testAccessibiltyContactFormFieldset() {
		checkAccessibility.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n" + "        <p>Lorem ipsum</p>\n"
				+ "        <p>Nivel de conformidad AA (doble A)</p>\n" + "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<fieldset>"
				+ "           No dudes en ponerte en contacto con nosotros" + "			<label for='nombre'>Nombre</label>\n"
				+ "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n" + "			\n" + "			<label for='mensaje'>Mensaje</label>\n"
				+ "			<input type='text' id='mensaje' name='mensaje' placeholder='Mensaje...'>\n" + "			</fieldset>" + "			\n" + "			\n"
				+ "			<input type='submit' value='Enviar'>\n" + "		</form>\n" + "    </body>\n" + "</html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
	}

	/**
	 * Test accessibilty contact form heading.
	 */
	@Test
	public void testAccessibiltyContactFormHeading() {
		checkAccessibility
				.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n" + "        <h3>Contacto</h3>" + "        <p>Lorem ipsum</p>\n"
						+ "        <p>Nivel de conformidad AA (doble A)</p>\n" + "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<fieldset>"
						+ "           Rellena los campos" + "			<label for='nombre'>Nombre</label>\n" + "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n"
						+ "			\n" + "			<label for='mensaje'>Mensaje</label>\n" + "			<input type='text' id='mensaje' name='mensaje' placeholder='Mensaje...'>\n"
						+ "			</fieldset>" + "			\n" + "			\n" + "			<input type='submit' value='Enviar'>\n" + "		</form>\n" + "    </body>\n" + "</html>");
		Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
		checkAccessibility.setContent("<html>\n" + "    <head>\n" + "        <title>Accesibilidad</title>\n" + "    </head>\n" + "    <body>\n"
				+ "        <span role='heading' aria-level='1'>Contacto</span>" + "        <p>Lorem ipsum</p>\n" + "        <p>Nivel de conformidad AA (doble A)</p>\n"
				+ "        <p>Fecha última revisión 01/01/2015</p>\n" + "        <form>\n" + "			<fieldset>" + "           Rellena los campos"
				+ "			<label for='nombre'>Nombre</label>\n" + "			<input type='text' id='nombre' name='nombre' placeholder='Nombre...'>\n" + "			\n"
				+ "			<label for='mensaje'>Mensaje</label>\n" + "			<input type='text' id='mensaje' name='mensaje' placeholder='Mensaje...'>\n" + "			</fieldset>" + "			\n"
				+ "			\n" + "			<input type='submit' value='Enviar'>\n" + "		</form>\n" + "    </body>\n" + "</html>");
		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ACCESSIBILITY_DECLARATION_NOT_CONTACT));
	}
}