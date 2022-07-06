
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
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.plugin.dao.RastreoDAO;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;

@Ignore
public class TestCrawlerJob {
	private CrawlerJob crawlerJob;

	@Ignore
	@Test
	public void testMakeCrawl() {
		Long idObservatorio = 5L;
		String nombreCartucho = "es.inteco.accesibilidad.CartuchoAccesibilidad";
		final PropertiesManager pmgr = new PropertiesManager();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/OAW", "root", "root");
			List<CuentaCliente> observatories = CuentaUsuarioDAO.getClientAccounts(c, idObservatorio, es.inteco.common.Constants.OBSERVATORY_TYPE);
			for (CuentaCliente observatory : observatories) {
				// Para que se resetee
				crawlerJob = new CrawlerJob();
				Logger.putLog("Inicio rastreo : " + observatory.getDatosRastreo().getId_rastreo(), TestCrawlerJob.class, Logger.LOG_LEVEL_INFO);
				observatory.getDatosRastreo().setCartuchos(new String[] { nombreCartucho });
				observatory.getDatosRastreo().setUrls(es.inteco.utils.CrawlerUtils
						.addDomainsToList(RastreoDAO.getList(c, (long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO), false, Constants.ID_LISTA_OBSERVATORIO));
				observatory.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils
						.addDomainsToList(RastreoDAO.getList(c, (long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO), false, Constants.ID_LISTA_OBSERVATORIO));
				observatory.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils
						.addDomainsToList(RastreoDAO.getList(c, (long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE), false, Constants.ID_LISTA_NO_RASTREABLE));
				observatory.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils
						.addDomainsToList(RastreoDAO.getList(c, (long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_RASTREABLE), false, Constants.ID_LISTA_RASTREABLE));
				observatory.getDatosRastreo().setId_guideline(8);
				observatory.getDatosRastreo().setFicheroNorma("observatorio-une-2012-b.xml");
				CrawlerData crawlerData = CrawlerUtils.getCrawlerData(observatory.getDatosRastreo(), 0L, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null);
				crawlerData.setTest(true);
				crawlerJob.makeCrawl(crawlerData);
				Logger.putLog("-------------------------------------------------------------------------------------------------------------------" + idObservatorio, TestCrawlerJob.class,
						Logger.LOG_LEVEL_INFO);
			}
		} catch (Exception e) {
			fail();
		}
	}
}
