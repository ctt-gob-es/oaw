package es.inteco.intav.checks.uneen2019;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;

/**
 * The Class AccesibilityChecksTest.
 */
public class AccesibilityChecksTest extends EvaluateCheck {
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
		checkAccessibility = TestUtils.getCheckAccessibility("observatorio-accesibilidad");
	}

	/**
	 * Evaluate meta.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void evaluateMatchAriaLabelWithLabel() throws Exception {
		checkAccessibility.setContent("<html>" + "<body><label for=\"example\">Match</label><input type=\"text\" id=\"example\" aria-label=\"Match\"/></body> " + "</html>");
		Assert.assertEquals(0, getNumProblems(checkAccessibility, 476));
		checkAccessibility.setContent("<html>" + "<body><label for=\"example\">Match</label><input type=\"text\" id=\"example\" aria-label=\"NoMatch\"/></body> " + "</html>");
		Assert.assertEquals(1, getNumProblems(checkAccessibility, 476));
	}
}
