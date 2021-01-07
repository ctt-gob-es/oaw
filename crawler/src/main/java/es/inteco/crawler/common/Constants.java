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

public interface Constants {
	int STATUS_NOT_LAUNCHED = 1;
	int STATUS_LAUNCHED = 2;
	int STATUS_STOPPED = 3;
	int STATUS_FINALIZED = 4;
	int STATUS_ERROR = 5;
	String CRAWLER_CORE_PROPERTIES = "crawler.core.properties";
	String MAIL_PROPERTIES = "mail.properties";
	String CRAWLER_JOB_NAME = "crawlerJob";
	String CRAWLER_JOB_GROUP = "crawlerJobGroup";
	String CRAWLER_JOB_TRIGGER = "crawlerJobTrigger";
	String CRAWLER_JOB_TRIGGER_GROUP = "crawlerJobTriggerGroup";
	String DEPTH = "depth";
	String THREADS = "threads";
	String TOPN = "topN";
	String CAT = "cat";
	String AS = "as";
	String TEXT = "text";
	String ID_CRAWLING = "idCrawling";
	String ID_FULFILLED_CRAWLING = "idFulfilledCrawling";
	String USER = "user";
	String CRAWLER_DATA = "crawlerData";
	String CUENTA_CLIENTE = "cuentaCliente";
	String OBSERVATORY_ID = "observatoryId";
	String OBSERVATORY = "observatory";
	String CARTRIDGE_ID = "cartridgeId";
	String EXECUTE_SCHEDULED_CRAWLING = "executeScheduledCrawling";
	String EXECUTE_SCHEDULED_CRAWLING_TRIGGER = "executeScheduledCrawlingTrigger";
	String SCHED_CRAWLER_JOB_NAME = "schedCrawlerJob";
	String SCHED_CRAWLER_JOB_TRIGGER = "schedCrawlerJobTrigger";
	String EXECUTE_SCHEDULED_OBSERVATORY = "executeScheduledObservatory";
	String EXECUTE_SCHEDULED_OBSERVATORY_GROUP = "ExecuteScheduledObservatory";
	String EXECUTE_SCHEDULED_OBSERVATORY_TRIGGER = "executeScheduledObservatoryTrigger";
	int ID_LISTA_SEMILLA = 1;
	int ID_LISTA_RASTREABLE = 2;
	int ID_LISTA_NO_RASTREABLE = 3;
	int ID_LISTA_OBSERVATORIO = 4;
	int FINISHED_OBSERVATORY_STATUS = 0;
	int LAUNCHED_OBSERVATORY_STATUS = 1;
	int ERROR_OBSERVATORY_STATUS = 2;
	int RELAUNCHED_OBSERVATORY_STATUS = 3;
	int STOPPED_OBSERVATORY_STATUS = 4;
	int OBS_FINALIZED_STATUS = 0;
	int OBS_LAUNCHED_STATUS = 1;
	int OBS_ERROR_STATUS = 2;
	String DEFAULT_CHARSET = "UTF-8";
}
