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
import es.inteco.rastreador2.job.DeleteTempDirJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;

public class DeleteTempDirServlet extends GenericServlet {

    private Scheduler scheduler;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        try {
            final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            final CronTrigger cronTrigger = new CronTrigger("DeleteFilesJob", "DeleteFilesJobGroup", getInitParameter("cronExpression"));
            final JobDetail jobDetail = new JobDetail("DeleteFilesJob", "DeleteFilesJobGroup", DeleteTempDirJob.class);
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            Logger.putLog("Se ha programado el job para el borrado de directorios temporales", DeleteTempDirServlet.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar crear el job para el borrado de directorios temporales", DeleteTempDirServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void destroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            Logger.putLog("FALLO al finalizar los Jobs (puede producir fallos de memoria)", DeleteTempDirServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
        super.destroy();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    }

}
