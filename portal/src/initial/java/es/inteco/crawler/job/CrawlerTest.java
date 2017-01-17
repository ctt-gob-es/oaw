package es.inteco.crawler.job;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CrawlerTest {

    final static String OBSERVATORIO_1_0 = "observatorio-inteco-1-0.xml";
    final static String OBSERVATORIO_1_0_NO_LINKS = "observatorio-inteco-1-0-nobroken.xml";
    final static String OBSERVATORIO_2_0 = "observatorio-une-2012.xml";
    final static String OBSERVATORIO_2_0_NO_LINKS = "observatorio-une-2012-nobroken.xml";

    //"http://www.congreso.es/portal/page/portal/Congreso/Congreso"

    @Test
    public void testCrawler() {
        final String url = "http://administracionelectronica.gob.es";
        final CrawlerJob crawlerJob = new CrawlerJob();
//        final List<CrawledLink> crawledLinkList = crawlerJob.testCrawler(createCrawlerData(url,OBSERVATORIO_1_0_NO_LINKS));
//        Assert.assertNotNull(crawledLinkList);
//        Assert.assertFalse(crawledLinkList.isEmpty());
    }

    private CrawlerData createCrawlerData(final String url, final String normativa) {
        final CrawlerData crawlerData = new CrawlerData();
        final String[] cartuchos = new String[]{"es.inteco.accesibilidad.CartuchoAccesibilidad"};
        crawlerData.setCartuchos(cartuchos);
        crawlerData.setIdCrawling(0);
        crawlerData.setIdFulfilledCrawling(0);
        crawlerData.setNombreRastreo("testCrawler");
        crawlerData.setLanguage("es");
        crawlerData.setProfundidad(4);
        crawlerData.setPseudoaleatorio(true);
        crawlerData.setTopN(4);
        crawlerData.setUser("test");
        crawlerData.setUsersMail(Collections.<String>emptyList());
        crawlerData.setTest(true);
        final List<String> urls = new ArrayList<String>();
        urls.add(url);
        crawlerData.setUrls(urls);
        crawlerData.setFicheroNorma(normativa);
        crawlerData.setDomains(null);
        crawlerData.setInDirectory(false);

        return crawlerData;
    }
}
