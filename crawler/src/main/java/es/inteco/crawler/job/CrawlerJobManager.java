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
package es.inteco.crawler.job;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import es.inteco.crawler.common.Constants;

/**
 * The Class CrawlerJobManager.
 */
public final class CrawlerJobManager {
	/** The scheduler. */
	private static Scheduler scheduler;

	/**
	 * Instantiates a new crawler job manager.
	 */
	private CrawlerJobManager() {
	}

	/**
	 * Inits the.
	 *
	 * @throws Exception the exception
	 */
	private static void init() throws Exception {
		if (scheduler == null) {
			final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		}
	}

	/**
	 * Start job.
	 *
	 * @param crawlerData the crawler data
	 * @throws Exception the exception
	 */
	public static void startJob(CrawlerData crawlerData) throws Exception {
		init();
		final JobDetail jobDetail = new JobDetail(Constants.CRAWLER_JOB_NAME + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_GROUP, CrawlerJob.class);
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constants.CRAWLER_DATA, crawlerData);
		jobDetail.setJobDataMap(jobDataMap);
		final Trigger trigger = new SimpleTrigger(Constants.CRAWLER_JOB_TRIGGER + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_TRIGGER_GROUP, new Date());
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * End job.
	 *
	 * @param idJob the id job
	 * @throws Exception the exception
	 */
	public static void endJob(long idJob) throws Exception {
		init();
		scheduler.interrupt(Constants.CRAWLER_JOB_NAME + "_" + idJob, Constants.CRAWLER_JOB_GROUP);
		scheduler.deleteJob(Constants.CRAWLER_JOB_NAME + "_" + idJob, Constants.CRAWLER_JOB_GROUP);
		List<JobExecutionContext> currentlyExecuting = scheduler.getCurrentlyExecutingJobs();
		// verifying if job is running
		for (JobExecutionContext jobExecutionContext : currentlyExecuting) {
			CrawlerData crawlerData = (CrawlerData) jobExecutionContext.getJobDetail().getJobDataMap().get(Constants.CRAWLER_DATA);
			if (crawlerData.getIdObservatory() == idJob) {
				scheduler.interrupt(jobExecutionContext.getJobDetail().getName(), jobExecutionContext.getJobDetail().getGroup());
				scheduler.deleteJob(jobExecutionContext.getJobDetail().getName(), jobExecutionContext.getJobDetail().getGroup());
			}
		}
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		for (Thread t : threads) {
			String name = t.getName();
			Thread.State state = t.getState();
			int priority = t.getPriority();
			String type = t.isDaemon() ? "Daemon" : "Normal";
			if ("RelanzarObservatorioThread".equals(t.getName())) {
				t.interrupt();
			}
			System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
		}
	}
}
