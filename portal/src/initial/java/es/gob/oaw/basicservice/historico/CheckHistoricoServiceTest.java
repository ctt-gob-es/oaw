package es.gob.oaw.basicservice.historico;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;

/**
 * Created by mikunis on 1/16/17.
 */
public class CheckHistoricoServiceTest {
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
		mysqlDataSource.setURL("jdbc:mysql://localhost:3306/OAW");
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("root");
		ic.bind("java:/comp/env/jdbc/oaw", mysqlDataSource);
	}

	@AfterClass
	public static void tearDownClass() throws NamingException {
		final InitialContext ic = new InitialContext();
		ic.destroySubcontext("java:");
	}

	@Test
	public void getHistoricoResultadosTest() throws Exception {
		final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
		final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados("http://www.fundacionctic.org", BasicServiceAnalysisType.URL, "4", "4", "observatorio-2");
		assertFalse(historicoResultados.isEmpty());
	}

	@Test
	public void isAnalysisOfUrlTest() throws Exception {
		final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
		assertTrue(checkHistoricoService.isAnalysisOfUrl("212", "http://www.fundacionctic.org", BasicServiceAnalysisType.URL, "4", "4", "observatorio-2"));
	}

	@Test
	public void getHistoricoResultadosListURLsTest() throws Exception {
		final String urls = "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n"
				+ "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n"
				+ "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org\n"
				+ "http://www.fundacionctic.org\n" + "http://www.fundacionctic.org";
		final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
		final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados(urls, BasicServiceAnalysisType.LISTA_URLS, "4", "4", "observatorio-2");
		assertFalse(historicoResultados.isEmpty());
	}
}