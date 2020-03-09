package es.inteco.rastreador2.actionform.basic.service;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mikunis on 1/17/17.
 */
public class BasicServiceAnalysisTypeTest {
    @Test
    public void getLabel() throws Exception {
        Assert.assertEquals("url", BasicServiceAnalysisType.URL.getLabel());
        Assert.assertEquals("código_fuente", BasicServiceAnalysisType.CODIGO_FUENTE.getLabel());
        Assert.assertEquals("lista_urls", BasicServiceAnalysisType.LISTA_URLS.getLabel());
    }

    @Test
    public void parseString() throws Exception {
        Assert.assertEquals(BasicServiceAnalysisType.URL, BasicServiceAnalysisType.parseString("url"));
        Assert.assertEquals(BasicServiceAnalysisType.CODIGO_FUENTE, BasicServiceAnalysisType.parseString("cÓdiGo_FuenTE"));
        Assert.assertEquals(BasicServiceAnalysisType.LISTA_URLS, BasicServiceAnalysisType.parseString("lista_URLS"));
    }

}