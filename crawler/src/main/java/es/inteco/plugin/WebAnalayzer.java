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
package es.inteco.plugin;

import java.util.HashMap;
import java.util.Map;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;

/**
 * The Class WebAnalayzer.
 *
 * @author a.mesas Clase que realiza analiza una web proporcionada por el crawler, esta clase ejecuta los cartuchos que estén configurados en el xml de conf. Así como se encarga de guardar los datos
 *         necesarios en BD
 */
public class WebAnalayzer {
	/** The cartuchos. */
	private final Map<String, Cartucho> cartuchos; // Tenemos esta estructura para reutilizar los cartuchos

	/**
	 * Instantiates a new web analayzer.
	 */
	public WebAnalayzer() {
		cartuchos = new HashMap<>();
	}

	/**
	 * Run cartuchos.
	 *
	 * @param crawledLink the crawled link
	 * @param fecha       the fecha
	 * @param crawlerData the crawler data
	 * @param cookie      the cookie
	 * @param isLast      the is last
	 */
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

	/**
	 * Gets the data.
	 *
	 * @param crawledLink         the crawled link
	 * @param idFulfilledCrawling the id fulfilled crawling
	 * @param fecha               the fecha
	 * @param crawlerData         the crawler data
	 * @param cookie              the cookie
	 * @param isLast              the is last
	 * @return the data
	 */
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
		datos.put("charset", crawledLink.getCharset());
		if (StringUtils.isNotEmpty(crawlerData.getAcronimo())) {
			datos.put("acronimo", crawlerData.getAcronimo());
		}
		if (crawlerData.getFicheroNorma() != null && !crawlerData.getFicheroNorma().isEmpty()) {
			datos.put("guidelineFile", crawlerData.getFicheroNorma());
		}
		// AÑADIDO PARA USER
		datos.put("user", crawlerData.getUser());
		if (crawlerData.getNombreRastreo().contains("-")) {
			datos.put("entity", crawlerData.getNombreRastreo().substring(0, crawlerData.getNombreRastreo().indexOf('-')));
		} else {
			datos.put("entity", crawlerData.getNombreRastreo());
		}
		return datos;
	}
}
