package es.inteco.rastreador2.pdf.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import es.ctic.basicservice.historico.CheckHistoricoService;
import es.ctic.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.Constants;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.pdf.basicservice.BasicServicePdfReport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by mikunis on 1/10/17.
 */
public class BasicServicePdfReportTest {

    private ObservatoryManager observatoryManager;

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

    @Before
    public void setUp() {
        observatoryManager = new ObservatoryManager();
    }

    @Test
    public void exportToPdfNoEvolution() throws Exception {
        final File exportFile = new File("/home/mikunis/Desktop/pruebaNoEvolution.pdf");
        exportFile.delete();
        Assert.assertFalse(exportFile.exists());
        final long analisisId = -131;
        final long basicServiceId = analisisId * -1;

        final BasicServiceForm basicServiceForm = DiagnosisDAO.getBasicServiceRequestById(DataBaseManager.getConnection(), basicServiceId);
        final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(new AnonymousResultExportPdfUNE2012(basicServiceForm));
        final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), analisisId);

        final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);

        basicServicePdfReport.exportToPdf(
                currentEvaluationPageList,
                Collections.<Date, List<ObservatoryEvaluationForm>>emptyMap(),
                exportFile.getAbsolutePath()
        );
        Assert.assertTrue(exportFile.exists());
    }

    @Test
    public void exportToPdfEvolution() throws Exception {
        final File exportFile = new File("/home/mikunis/Desktop/pruebaEvolution.pdf");
        final File tempFile = new File("/home/mikunis/Desktop/temp");
        tempFile.delete();
        exportFile.delete();
        Assert.assertFalse(exportFile.exists());
        final long basicServiceId = 211;
        final long analisisId = basicServiceId * -1;

        final BasicServiceForm basicServiceForm = DiagnosisDAO.getBasicServiceRequestById(DataBaseManager.getConnection(), basicServiceId);
        final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), analisisId);

        final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);

        final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
        final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);

        Assert.assertFalse(previousEvaluationsPageList.isEmpty());
        Assert.assertEquals(2, previousEvaluationsPageList.size());
        final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(new AnonymousResultExportPdfUNE2012(basicServiceForm));
        basicServicePdfReport.exportToPdf(
                currentEvaluationPageList,
                previousEvaluationsPageList,
                exportFile.getAbsolutePath()
        );
        Assert.assertTrue(exportFile.exists());
    }

}