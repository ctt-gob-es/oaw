package es.inteco.crawler.job;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
        final String url = "http://www.minhafp.gob.es";
        final CrawlerJob crawlerJob = new CrawlerJob();
        final List<CrawledLink> crawledLinkList = crawlerJob.testCrawler(createCrawlerData(url,OBSERVATORIO_2_0_NO_LINKS));
        Assert.assertNotNull(crawledLinkList);
        Assert.assertFalse(crawledLinkList.isEmpty());
        for (CrawledLink crawledLink : crawledLinkList) {
            System.out.println(crawledLink);
        }

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
    
    
	@Test
	public void testSameDirectory() {

		java.util.List<String> urls = new ArrayList<String>();

		urls.add("http://www.fundacionctic.org/");
		urls.add("http://www.fundacionctic.org/lineas-de-especializacion/analisis-inteligente-de-datos");

		Assert.assertTrue(checkUrlList(urls, "http://www.fundacionctic.org/lineas-de-especializacion/interoperabilidad-od"));
		Assert.assertFalse(checkUrlList(urls, "http://www.fundacionctic.org/focos/cities"));

	}

	private boolean checkUrlList(java.util.List<String> urls, String urlLink) {
		
		if (urlLink.lastIndexOf("/") > 0) {
			String directorio = urlLink.substring(0, urlLink.lastIndexOf("/")+1);
			
			// Si el patrón está en la lista este de descarta temporalmente
			for (String link : urls) {
				if (!StringUtils.isEmpty(link) && link.startsWith(directorio)) {
					return true;

				}
			}
		}

		return false;
	}
}
