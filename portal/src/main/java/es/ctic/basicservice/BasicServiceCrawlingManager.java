package es.ctic.basicservice;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicServiceCrawlingManager {

    /**
     * Realiza el crawling correspondiente a una petición del servicio de diagnóstico
     *
     * @param basicServiceForm los parámetros de la petición del servicio de diagnóstico
     * @return una lista con las páginas que se han recorrido
     */
    public List<CrawledLink> getCrawledLinks(final BasicServiceForm basicServiceForm) {
        final List<CrawledLink> crawledLinks;
        final CrawlerJob crawlerJob = new CrawlerJob();
        final CrawlerData crawlerData = createCrawlerData(basicServiceForm);

        if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.URL) {
            crawledLinks = crawlerJob.testCrawler(crawlerData);
        } else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.LISTA_URLS) {
            // Si es una lista de urls modificamos la información para que no se realice crawling.
            disableCrawling(crawlerData);
            crawledLinks = crawlerJob.testCrawler(crawlerData);
        } else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.CODIGO_FUENTE) {
            crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
        } else {
            crawledLinks = Collections.emptyList();
        }
        return crawledLinks;
    }

    private CrawlerData createCrawlerData(final BasicServiceForm basicServiceForm) {
        // La variable idCrawling es el campo cod_rastreo en la tabla tanalisis
        final long idCrawling = basicServiceForm.getId() * (-1);
        final CrawlerData crawlerData = new CrawlerData();
        final String[] cartuchos = new String[]{"es.inteco.accesibilidad.CartuchoAccesibilidad"};
        crawlerData.setCartuchos(cartuchos);
        crawlerData.setIdCrawling(idCrawling);
        crawlerData.setIdFulfilledCrawling(idCrawling);
        crawlerData.setNombreRastreo(basicServiceForm.getName());
        crawlerData.setLanguage(basicServiceForm.getLanguage());
        crawlerData.setProfundidad(Integer.parseInt(basicServiceForm.getProfundidad()));
        crawlerData.setPseudoaleatorio(true);
        crawlerData.setTopN(Integer.parseInt(basicServiceForm.getAmplitud()));
        crawlerData.setUser(basicServiceForm.getUser());
        crawlerData.setUsersMail(Collections.singletonList(basicServiceForm.getEmail()));
        crawlerData.setTest(true);
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            final List<String> urls = new ArrayList<>();
            final String[] split = basicServiceForm.getDomain().split("\r\n");
            Collections.addAll(urls, split);
            // En el caso de lista cerrada nos quedamos con 17 urls como máximo
            crawlerData.setUrls(urls.subList(0, Math.min(urls.size(),17)));
        }
        crawlerData.setContent(basicServiceForm.getContent());

        final long idGuideline = BasicServiceUtils.getGuideline(basicServiceForm.getReport());
        crawlerData.setFicheroNorma(includeBrokenLinksCheck(CrawlerUtils.getFicheroNorma(idGuideline), basicServiceForm.getReport()));
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(basicServiceForm.getDomain(), true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setInDirectory(basicServiceForm.isInDirectory());
        Logger.putLog(crawlerData.getFicheroNorma(), BasicServiceCrawlingManager.class, Logger.LOG_LEVEL_DEBUG);
        return crawlerData;
    }

    private String includeBrokenLinksCheck(final String ficheroNorma, final String report) {
        if (report.endsWith("-nobroken")) {
            return ficheroNorma.substring(0, ficheroNorma.length() - 4) + "-nobroken.xml";
        } else {
            return ficheroNorma;
        }
    }

    private void disableCrawling(final CrawlerData crawlerData) {
        crawlerData.setProfundidad(1);
        crawlerData.setTopN(1);
        crawlerData.setPseudoaleatorio(false);
    }
}