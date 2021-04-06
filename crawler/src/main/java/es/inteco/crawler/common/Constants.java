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
package es.inteco.crawler.common;

/**
 * The Interface Constants.
 */
public interface Constants {
	/** The status not launched. */
	int STATUS_NOT_LAUNCHED = 1;
	/** The status launched. */
	int STATUS_LAUNCHED = 2;
	/** The status stopped. */
	int STATUS_STOPPED = 3;
	/** The status finalized. */
	int STATUS_FINALIZED = 4;
	/** The status error. */
	int STATUS_ERROR = 5;
	/** The crawler core properties. */
	String CRAWLER_CORE_PROPERTIES = "crawler.core.properties";
	/** The mail properties. */
	String MAIL_PROPERTIES = "mail.properties";
	/** The crawler job name. */
	String CRAWLER_JOB_NAME = "crawlerJob";
	/** The crawler job group. */
	String CRAWLER_JOB_GROUP = "crawlerJobGroup";
	/** The crawler job trigger. */
	String CRAWLER_JOB_TRIGGER = "crawlerJobTrigger";
	/** The crawler job trigger group. */
	String CRAWLER_JOB_TRIGGER_GROUP = "crawlerJobTriggerGroup";
	/** The depth. */
	String DEPTH = "depth";
	/** The threads. */
	String THREADS = "threads";
	/** The topn. */
	String TOPN = "topN";
	/** The cat. */
	String CAT = "cat";
	/** The as. */
	String AS = "as";
	/** The text. */
	String TEXT = "text";
	/** The id crawling. */
	String ID_CRAWLING = "idCrawling";
	/** The id fulfilled crawling. */
	String ID_FULFILLED_CRAWLING = "idFulfilledCrawling";
	/** The user. */
	String USER = "user";
	/** The crawler data. */
	String CRAWLER_DATA = "crawlerData";
	/** The cuenta cliente. */
	String CUENTA_CLIENTE = "cuentaCliente";
	/** The observatory id. */
	String OBSERVATORY_ID = "observatoryId";
	/** The observatory. */
	String OBSERVATORY = "observatory";
	/** The cartridge id. */
	String CARTRIDGE_ID = "cartridgeId";
	/** The execute scheduled crawling. */
	String EXECUTE_SCHEDULED_CRAWLING = "executeScheduledCrawling";
	/** The execute scheduled crawling trigger. */
	String EXECUTE_SCHEDULED_CRAWLING_TRIGGER = "executeScheduledCrawlingTrigger";
	/** The sched crawler job name. */
	String SCHED_CRAWLER_JOB_NAME = "schedCrawlerJob";
	/** The sched crawler job trigger. */
	String SCHED_CRAWLER_JOB_TRIGGER = "schedCrawlerJobTrigger";
	/** The execute scheduled observatory. */
	String EXECUTE_SCHEDULED_OBSERVATORY = "executeScheduledObservatory";
	/** The execute scheduled observatory group. */
	String EXECUTE_SCHEDULED_OBSERVATORY_GROUP = "ExecuteScheduledObservatory";
	/** The execute scheduled observatory trigger. */
	String EXECUTE_SCHEDULED_OBSERVATORY_TRIGGER = "executeScheduledObservatoryTrigger";
	/** The id lista semilla. */
	int ID_LISTA_SEMILLA = 1;
	/** The id lista rastreable. */
	int ID_LISTA_RASTREABLE = 2;
	/** The id lista no rastreable. */
	int ID_LISTA_NO_RASTREABLE = 3;
	/** The id lista observatorio. */
	int ID_LISTA_OBSERVATORIO = 4;
	/** The finished observatory status. */
	int FINISHED_OBSERVATORY_STATUS = 0;
	/** The launched observatory status. */
	int LAUNCHED_OBSERVATORY_STATUS = 1;
	/** The error observatory status. */
	int ERROR_OBSERVATORY_STATUS = 2;
	/** The relaunched observatory status. */
	int RELAUNCHED_OBSERVATORY_STATUS = 3;
	/** The stopped observatory status. */
	int STOPPED_OBSERVATORY_STATUS = 4;
	/** The obs finalized status. */
	int OBS_FINALIZED_STATUS = 0;
	/** The obs launched status. */
	int OBS_LAUNCHED_STATUS = 1;
	/** The obs error status. */
	int OBS_ERROR_STATUS = 2;
	/** The default charset. */
	String DEFAULT_CHARSET = "UTF-8";
}
