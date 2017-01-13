package es.inteco.rastreador2.pdf.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import es.ctic.basicservice.historico.BasicServiceResultado;
import es.ctic.basicservice.historico.CheckHistoricoAction;
import es.ctic.basicservice.historico.CheckHistoricoService;
import es.ctic.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.utils.CrawlerUtils;
import junit.framework.Assert;
import org.apache.struts.util.MessageResources;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


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

    @Before
    public void setUp() {
        observatoryManager = new ObservatoryManager();
    }

    @Test
    public void exportToPdf() throws Exception {
        final long analisisId = -131;
        final long basicServiceId = analisisId * -1;

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        final BasicServiceForm basicServiceForm = DiagnosisDAO.getBasicServiceRequestById(DataBaseManager.getConnection(), basicServiceId);
        final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(new AnonymousResultExportPdfUNE2012(basicServiceForm));
        final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), analisisId);

        CheckHistoricoService checkHistoricoService = new CheckHistoricoService();

        final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
        final Map<Date,List<ObservatoryEvaluationForm>> previousEvaluationsPageList = new TreeMap<>();
        final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados(basicServiceForm.getDomain());
        Assert.assertEquals(3,historicoResultados.size());
        for (BasicServiceResultado historicoResultado : historicoResultados.subList(1,3)) {
            final List<Long> analysisIds = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), Long.parseLong(historicoResultado.getId()));
            previousEvaluationsPageList.put(dateFormat.parse(historicoResultado.getDate()), observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIds));
        }
        Assert.assertFalse(previousEvaluationsPageList.isEmpty());
        Assert.assertEquals(2, previousEvaluationsPageList.size());

        basicServicePdfReport.exportToPdf(
                currentEvaluationPageList,
                previousEvaluationsPageList,
                MessageResources.getMessageResources("ApplicationResources"),
                "/home/mikunis/Desktop/prueba.pdf"
                );
    }

}