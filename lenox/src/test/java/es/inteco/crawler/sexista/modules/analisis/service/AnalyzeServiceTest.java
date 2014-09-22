package es.inteco.crawler.sexista.modules.analisis.service;


import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class AnalyzeServiceTest {

    private AnalyzeService service;
    private TerminosService terminosService;
    private ExceptionServiceMock exceptionService;
    private TerminosDao terminosDao;

    @Before
    public void setUp() throws Exception {
        this.service = new AnalyzeService();
        this.exceptionService = new ExceptionServiceMock();
        this.terminosService = new TerminosService();
        this.terminosDao = new TerminosDao();

        this.terminosService.setDao(terminosDao);

        this.service.setTerminosService(terminosService);
        this.service.setExceptionService(exceptionService);
    }

    @After
    public void tearDown() throws Exception {
        this.service = null;
    }

    /**
     * Comprobamos que diferenciamos entre singular y plural de un mismo termino encontrado.
     * <p/>
     * NOTA: El termino abogado aparece dos veces en el listado de agrupados, ya que los agrupa
     * diferenciando entre singulares y plurales
     *
     * @throws Exception
     */
    @Test
    public void procesarContenidoTest() throws Exception {

        //Parametros de entrada
        String texto = "Los abogados españoles interponen recursos al abogado que mas le apetece.";

        //Ejecutamos el metodo
        // this.service.procesarContenido(texto);
        Assert.assertNotNull(this.exceptionService);
        Assert.assertNotNull(this.exceptionService.lstGroupTerminosMock);
        //Verificamos
        Assert.assertEquals(3, this.exceptionService.lstGroupTerminosMock.size());
        Assert.assertEquals("Abogado", this.exceptionService.lstGroupTerminosMock.get(0).getTermino());
        Assert.assertFalse(this.exceptionService.lstGroupTerminosMock.get(0).isFoundInSingular());
        Assert.assertEquals("Abogado", this.exceptionService.lstGroupTerminosMock.get(1).getTermino());
        Assert.assertTrue(this.exceptionService.lstGroupTerminosMock.get(1).isFoundInSingular());
        Assert.assertEquals("Español", this.exceptionService.lstGroupTerminosMock.get(2).getTermino());
        Assert.assertFalse(this.exceptionService.lstGroupTerminosMock.get(2).isFoundInSingular());

    }

    public class ExceptionServiceMock extends ExceptionService {

        public ArrayList<TerminoPositionDto> lstGroupTerminosMock;
        public ArrayList<String> lstTerminosMock;

		/*@Override
        public ArrayList<TerminoPositionDto> filterException(
				ArrayList<String> lstTerminos,
				ArrayList<TerminoPositionDto> lstGroupTerminos)
				throws BusinessException {
			
			lstTerminosMock = lstTerminos;
			lstGroupTerminosMock = lstGroupTerminos;
			
			return null;
		}*/
    }
}
