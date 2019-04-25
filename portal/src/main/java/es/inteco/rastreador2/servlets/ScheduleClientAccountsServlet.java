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
package es.inteco.rastreador2.servlets;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.job.ExecuteScheduledCrawling;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class ScheduleClientAccountsServlet extends GenericServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void scheduleJob(long idAccount, int type) {
        try (Connection c = DataBaseManager.getConnection()) {
            final List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, idAccount, type);
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                scheduleJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para la cuenta cliente " + idAccount, ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static void scheduleJobs(List<CuentaCliente> cuentasCliente) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        for (CuentaCliente cuentaCliente : cuentasCliente) {
            if (cuentaCliente.isActive()) {
                scheduleJob(scheduler, cuentaCliente);
            }
        }
    }

    private static void scheduleJob(Scheduler scheduler, CuentaCliente cuentaCliente) {
        Logger.putLog("Programando el job para la cuenta " + cuentaCliente.getNombre(), ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_INFO);

        try {
            final String jobName = String.format("%s_%d_%d", Constants.EXECUTE_SCHEDULED_CRAWLING, cuentaCliente.getIdCuenta(), cuentaCliente.getDatosRastreo().getId_rastreo());
            final JobDetail jobDetail = new JobDetail(jobName, "ExecuteScheduledCrawling", ExecuteScheduledCrawling.class);

            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(Constants.CUENTA_CLIENTE, cuentaCliente);
            jobDetail.setJobDataMap(jobDataMap);

            final Trigger trigger;
            final String triggerName = String.format("%s_%d_%d", Constants.EXECUTE_SCHEDULED_CRAWLING_TRIGGER, cuentaCliente.getIdCuenta(), cuentaCliente.getDatosRastreo().getId_rastreo());
            if (StringUtils.isNotEmpty(cuentaCliente.getPeriodicidad().getCronExpression())) {
                String cronExpression = CrawlerUtils.getCronExpression(cuentaCliente.getFecha(), cuentaCliente.getPeriodicidad().getCronExpression());
                Logger.putLog("Estableciendo expresión de cron: " + cronExpression, ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_INFO);
                trigger = new CronTrigger(triggerName, Constants.CRAWLER_JOB_TRIGGER_GROUP, cronExpression);
            } else {
                trigger = new SimpleTrigger(triggerName,
                        Constants.CRAWLER_JOB_TRIGGER_GROUP, cuentaCliente.getFecha(), new Date(Long.MAX_VALUE), SimpleTrigger.REPEAT_INDEFINITELY,
                        (long) cuentaCliente.getPeriodicidad().getDias() * 24 * 60 * 60 * 1000);
            }

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            Logger.putLog("Error al programar el job", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void deleteJobs(long idAccount, int type) {
        try (Connection c = DataBaseManager.getConnection()) {
            List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, idAccount, type);
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                deleteJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al eliminar los jobs asociados a cuentas clientes borradas", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static void deleteJobs(List<CuentaCliente> cuentasCliente) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        for (CuentaCliente cuentaCliente : cuentasCliente) {
            scheduler.deleteJob(String.format("%s_%d_%d", Constants.EXECUTE_SCHEDULED_CRAWLING, cuentaCliente.getIdCuenta(), cuentaCliente.getDatosRastreo().getId_rastreo()),
                    "ExecuteScheduledCrawling");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try (Connection c = DataBaseManager.getConnection()) {
            List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, es.inteco.common.Constants.ALL_DATA, es.inteco.common.Constants.CLIENT_ACCOUNT_TYPE);
            // cuentasCliente.addAll(CuentaUsuarioDAO.getClientAccounts(c, es.inteco.common.Constants.ALL_DATA, es.inteco.common.Constants.OBSERVATORY_TYPE));
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                scheduleJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente y observatorios", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
    }
}
