package es.ctic.basicservice.historico;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.List;

import static org.junit.Assert.*;

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

    @Test
    public void getHistoricoResultadosTest() throws Exception {
        final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
        final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados("http://www.fundacionctic.org");
        assertTrue(!historicoResultados.isEmpty());
    }

    @Test
    public void isAnalysisOfUrlTest() throws Exception {
        final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
        assertTrue(checkHistoricoService.isAnalysisOfUrl("148", "http://www.fundacionctic.org"));
    }

}