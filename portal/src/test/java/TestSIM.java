import java.util.Collections;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.struts.util.MessageResources;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import junit.framework.Assert;

/**
 * Created by alvaro on 2/7/17.
 */
public class TestSIM {
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

	@Ignore
	@Test
	public void testExportEvolutionPdf() {
		final long idRastreoRealizado = 70;
		final long idRastreoPrevio = 73;
		final long idObservatoryExecution = 2;
		try {
			final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
			final List<Long> previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoPrevio);
			Assert.assertTrue("No hay evaluaciones", !evaluationIds.isEmpty());
			Assert.assertTrue("No hay evaluaciones previas y no se genera evolutivo", !previousEvaluationIds.isEmpty());
			PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
					MessageResources.getMessageResources("ApplicationResources"), "/home/alvaro/Desktop/test_evolution_results.pdf", "Seed", "", idObservatoryExecution, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testExportPreviousPdf() {
		final long idRastreoRealizado = 73;
		final long idObservatoryExecution = 1;
		try {
			final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
			final List<Long> previousEvaluationIds = Collections.emptyList();
			Assert.assertTrue("No hay evaluaciones", !evaluationIds.isEmpty());
			PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
					MessageResources.getMessageResources("ApplicationResources"), "/home/alvaro/Desktop/test_initial_results.pdf", "Seed", "", idObservatoryExecution, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
