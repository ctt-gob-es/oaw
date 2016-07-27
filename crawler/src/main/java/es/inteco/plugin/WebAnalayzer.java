package es.inteco.plugin;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author a.mesas
 *         Clase que realiza analiza una web proporcionada por el crawler,
 *         esta clase ejecuta los cartuchos que estén configurados en el xml de conf.
 *         Así como se encarga de guardar los datos necesarios en BD
 */
public class WebAnalayzer {
    private final Map<String, Cartucho> cartuchos;  //Tenemos esta estructura para reutilizar los cartuchos

    public WebAnalayzer() {
        cartuchos = new HashMap<>();
    }

    public void runCartuchos(CrawledLink crawledLink, String fecha, CrawlerData crawlerData, String cookie, boolean isLast) {
        final String fechaReplaced = fecha.replace("_", " ");
        final Map<String, Object> datos = getData(crawledLink, crawlerData.getIdFulfilledCrawling(), fechaReplaced, crawlerData, cookie, isLast);

        try {
            // tendremos tantos cartuchos sean necearios, leidos de xml de configuracion
            for (String nombreCartucho : crawlerData.getCartuchos()) {
                Cartucho analizador = cartuchos.get(nombreCartucho);

                if (analizador == null) {
                    analizador = FactoryCartuchos.getCartucho(nombreCartucho);
                    analizador.setConfig(crawlerData.getIdCrawling());
                }

                cartuchos.put(nombreCartucho, analizador);
                analizador.analyzer(datos);
            }
        } catch (Exception e) {
            Logger.putLog("Fallo al ejecutar el cartucho", WebAnalayzer.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private Map<String, Object> getData(CrawledLink crawledLink, long idFulfilledCrawling, String fecha, CrawlerData crawlerData, String cookie, boolean isLast) {
        final Map<String, Object> datos = new HashMap<>();

        datos.put("id_rastreo", crawlerData.getIdCrawling());
        datos.put("idFulfilledCrawling", idFulfilledCrawling);
        datos.put("idObservatory", crawlerData.getIdObservatory());
        datos.put("reintentos", crawledLink.getNumRetries());
        datos.put("redirecciones", crawledLink.getNumRedirections());
        datos.put("fecha_hora", fecha);
        datos.put("url", crawledLink.getUrl());
        datos.put("cookie", cookie);
        datos.put("contenido", crawledLink.getSource());
        datos.put("isLast", isLast);
        if (StringUtils.isNotEmpty(crawlerData.getAcronimo())) {
            datos.put("acronimo", crawlerData.getAcronimo());
        }

        if (crawlerData.getFicheroNorma() != null && !crawlerData.getFicheroNorma().isEmpty()) {
            datos.put("guidelineFile", crawlerData.getFicheroNorma());
        }

        //AÑADIDO PARA USER
        datos.put("user", crawlerData.getUser());
        if (crawlerData.getNombreRastreo().contains("-")) {
            datos.put("entity", crawlerData.getNombreRastreo().substring(0, crawlerData.getNombreRastreo().indexOf('-')));
        } else {
            datos.put("entity", crawlerData.getNombreRastreo());
        }

        return datos;
    }

}


