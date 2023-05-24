/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.basicservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerFile;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceFile;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

/**
 * The Class BasicServiceCrawlingManager.
 */
public class BasicServiceCrawlingManager {
	/** The max url. */
	private int maxUrl;

	/**
	 * Realiza el crawling correspondiente a una petición del servicio de diagnóstico.
	 *
	 * @param basicServiceForm los parámetros de la petición del servicio de diagnóstico
	 * @return una lista con las páginas que se han recorrido
	 * @throws SQLException the SQL exception
	 * @throws Exception    the exception
	 */
	public List<CrawledLink> getCrawledLinks(final BasicServiceForm basicServiceForm) throws SQLException, Exception {
		// Cambio de numero de urls maximas a anilizar
		PropertiesManager pmgr = new PropertiesManager();
		maxUrl = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
		List<CrawledLink> crawledLinks = new ArrayList<CrawledLink>();
		final CrawlerJob crawlerJob = new CrawlerJob();
		final CrawlerData crawlerData = createCrawlerData(basicServiceForm);
		if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.URL) {
			crawledLinks = crawlerJob.testCrawler(crawlerData);
		} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.LISTA_URLS) {
			// Si es una lista de urls modificamos la información para que no se
			// realice crawling.
			disableCrawling(crawlerData);
			crawledLinks = crawlerJob.testCrawler(crawlerData);
		} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.CODIGO_FUENTE) {
			crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
		} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.CODIGO_FUENTE_MULTIPLE) {
			crawledLinks = crawlerJob.runMultiplaFilesAnalysis(crawlerData);
		} else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.MIXTO) {
			if (basicServiceForm.getContents() != null) {
				crawledLinks = crawlerJob.runMultiplaFilesAnalysis(crawlerData);
			} else if (basicServiceForm.getContent() != null) {
				crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
			}
			disableCrawling(crawlerData);
			crawledLinks.addAll(crawlerJob.testCrawler(crawlerData));
		} else {
			crawledLinks = Collections.emptyList();
		}
		return crawledLinks;
	}

	/**
	 * Creates the crawler data.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the crawler data
	 * @throws SQLException the SQL exception
	 * @throws Exception    the exception
	 */
	private CrawlerData createCrawlerData(final BasicServiceForm basicServiceForm) throws SQLException, Exception {
		// La variable idCrawling es el campo cod_rastreo en la tabla tanalisis
		final long idCrawling = basicServiceForm.getId() * (-1);
		final CrawlerData crawlerData = new CrawlerData();
		final String[] cartuchos = new String[] { "es.inteco.accesibilidad.CartuchoAccesibilidad" };
		crawlerData.setCartuchos(cartuchos);
		crawlerData.setIdCrawling(idCrawling);
		crawlerData.setIdFulfilledCrawling(idCrawling);
		crawlerData.setNombreRastreo(basicServiceForm.getName());
		crawlerData.setLanguage(basicServiceForm.getLanguage());
		crawlerData.setPseudoaleatorio(true);
		crawlerData.setUser(basicServiceForm.getUser());
		crawlerData.setUsersMail(Collections.singletonList(basicServiceForm.getEmail()));
		crawlerData.setTest(true);
		if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
			final List<String> urls = new ArrayList<>();
			final String[] split = basicServiceForm.getDomain().split("\r\n");
			Collections.addAll(urls, split);
			// Cambio de numero de urls maximas a anilizar
			crawlerData.setUrls(urls.subList(0, Math.min(urls.size(), maxUrl)));
		}
		crawlerData.setContent(basicServiceForm.getContent());
		final long idGuideline = BasicServiceUtils.getGuideline(basicServiceForm.getReport());
		crawlerData.setFicheroNorma(includeBrokenLinksCheck(CrawlerUtils.getFicheroNorma(idGuideline), basicServiceForm.getReport()));
		crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(basicServiceForm.getDomain(), true, Constants.ID_LISTA_SEMILLA));
		crawlerData.setInDirectory(basicServiceForm.isInDirectory());
		if (BasicServiceAnalysisType.CODIGO_FUENTE_MULTIPLE.equals(basicServiceForm.getAnalysisType()) || BasicServiceAnalysisType.MIXTO.equals(basicServiceForm.getAnalysisType())) {
			if (basicServiceForm.getContents() != null && !basicServiceForm.getContents().isEmpty()) {
				List<CrawlerFile> contents = new ArrayList<>();
				for (BasicServiceFile file : basicServiceForm.getContents()) {
					CrawlerFile cf = new CrawlerFile();
					BeanUtils.copyProperties(cf, file);
					contents.add(cf);
				}
				crawlerData.setContents(contents);
			}
		}
		// Calculate complex
		final String complexity = basicServiceForm.getComplexity();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(complexity)) {
			ComplejidadForm comp = ComplejidadDAO.getById(DataBaseManager.getConnection(), complexity);
			if (comp != null) {
				crawlerData.setTopN(comp.getAmplitud());
				crawlerData.setProfundidad(comp.getProfundidad());
			} else {
				crawlerData.setTopN(0);
				crawlerData.setProfundidad(0);
			}
		} else {
			crawlerData.setTopN(0);
			crawlerData.setProfundidad(0);
		}
		return crawlerData;
	}

	/**
	 * Include broken links check.
	 *
	 * @param ficheroNorma the fichero norma
	 * @param report       the report
	 * @return the string
	 */
	private String includeBrokenLinksCheck(final String ficheroNorma, final String report) {
		if (report.endsWith("-nobroken")) {
			return ficheroNorma.substring(0, ficheroNorma.length() - 4) + "-nobroken.xml";
		} else {
			return ficheroNorma;
		}
	}

	/**
	 * Disable crawling.
	 *
	 * @param crawlerData the crawler data
	 */
	private void disableCrawling(final CrawlerData crawlerData) {
		crawlerData.setProfundidad(1);
		crawlerData.setTopN(1);
		crawlerData.setPseudoaleatorio(false);
	}
}