/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TestSeeds {
    public static void main(String[] args) throws Exception {
        //System.setProperty("http.proxyHost", "172.23.50.61");
        //System.setProperty("http.proxyPort", "8080");
        //System.setProperty("https.proxyHost", "172.23.50.61");
        //System.setProperty("https.proxyPort", "8080");

        List<String> checksPaths = new ArrayList<String>();
        checksPaths.add("/home/sergio/Escritorio/semillas/principales.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/boletines.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/educacion.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/empleo.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/parlamentos.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/salud.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/sedes.xml");
        checksPaths.add("/home/sergio/Escritorio/semillas/tributos.xml");

        FileWriter writer = null;
        for (String checksPath : checksPaths) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document docAllChecks = builder.parse(new File(checksPath));
            NodeList seeds = docAllChecks.getElementsByTagName("SEMILLA");
            for (int i = 0; i < seeds.getLength(); i++) {
                CrawlerJob crawlerJob = new CrawlerJob();
                try {
                    writer = new FileWriter("/home/sergio/Escritorio/semillas/testSeeds.txt", true);
                    NodeList urls = ((Element) seeds.item(i)).getElementsByTagName("URL");
                    List<CrawledLink> crawledLinks = null;
                    CrawlerData crawlerData = null;
                    String url = null;
                    for (int j = 0; j < urls.getLength(); j++) {
                        url = ((Element) urls.item(j)).getTextContent();
                        System.out.println("Comenzando el rastreo para " + url);
                        writer.write("Comenzando el rastreo para " + url + "\n");
                        crawlerData = createCrawlerData(url);
                        crawledLinks = crawlerJob.testCrawler(crawlerData);
                    }

                    if (crawledLinks.size() < (crawlerData.getTopN() * crawlerData.getProfundidad() + 1)) {
                        System.out.println("#### " + url + " solo ha encontrado " + crawledLinks.size() + " páginas");
                        writer.write("#### " + url + " solo ha encontrado " + crawledLinks.size() + " páginas\n");
                    }
                    writer.flush();
                } catch (Exception e) {

                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }


    }

    private static CrawlerData createCrawlerData(String url) {
        CrawlerData crawlerData = new CrawlerData();
        crawlerData.setProfundidad(3);
        crawlerData.setTopN(3);
        crawlerData.setPseudoaleatorio(true);
        crawlerData.setExhaustive(false);
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(url, true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setNombreRastreo("test");
        crawlerData.setUser("test");
        List<String> urls = new ArrayList<String>();
        urls.add(url);
        crawlerData.setUrls(urls);

        return crawlerData;
    }
}