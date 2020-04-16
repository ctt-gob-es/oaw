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
package es.inteco.intav.checks.uneen2019;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;

/**
 * The Class Check_1_9.
 */
public final class Check_2_2 extends EvaluateCheck {

	/** The check accessibility. */
	private CheckAccessibility checkAccessibility;

	/**
	 * Sets the up class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		// Create initial context
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		final InitialContext ic = new InitialContext();

		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");

		// Construct DataSource
		final MysqlConnectionPoolDataSource mysqlDataSource = new MysqlConnectionPoolDataSource();
		mysqlDataSource.setURL("jdbc:mysql://localhost:3306/oaw_js");
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("root");

		ic.bind("java:/comp/env/jdbc/oaw", mysqlDataSource);
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		EvaluatorUtility.initialize();
		checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-en2019");
	}

	/**
	 * Evaluate meta.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void evaluateMatchAriaLabelWithLabel() throws Exception {

		Evaluation evaluation;
		//'line-height', 'letter-spacing', 'word-spacing'

//		checkAccessibility.setContent(
//				"<html><head><style> body { line-height: 1em; letter-spacing:1em; word-spacing:1em; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
//		evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//		Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), 477));
//
//		checkAccessibility.setContent("<html><head><style> body { line-height: 1em !important; letter-spacing:1em; word-spacing:1em; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
//        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), 477));
//
//        checkAccessibility.setContent("<html><head><style> body { line-height: 1em !important; letter-spacing:1em !important; word-spacing:1em; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
//        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), 477));
//
//
//        checkAccessibility.setContent("<html><head><style> body { line-height: 1em !important; letter-spacing:1em !important; word-spacing:1em !important; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
//        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
//        Assert.assertEquals(3, TestUtils.getNumProblems(evaluation.getProblems(), 477));


        checkAccessibility.setContent("<html><head><link href=\"http://tawmonitorurl.local/check22.css\" rel=\"stylesheet\" type=\"text/css\"/><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
		Assert.assertEquals(3, TestUtils.getNumProblems(evaluation.getProblems(), 477));
	}

}