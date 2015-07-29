package es.inteco.crawler.sexista.modules.analisis.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = {
        AnalyzeServiceTerminosSimplesTest.class,
        AnalyzeServiceTerminosCompuestosTest.class,
        AnalyzeServiceLocalAndGlobalExceptionTest.class,
        AnalyzeServiceHeuristicasTest.class,
        AnalyzeServiceFormaUnicaTest.class,
        AnalyzeServiceOtherExceptionTest.class
}
)
public class InitAllServiceTest {

}
