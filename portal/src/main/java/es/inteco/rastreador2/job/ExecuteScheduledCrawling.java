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
package es.inteco.rastreador2.job;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.login.Role;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.servlets.ScheduleClientAccountsServlet;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ExecuteScheduledCrawling implements StatefulJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        final CuentaCliente cuentaCliente = (CuentaCliente) jobDataMap.get(Constants.CUENTA_CLIENTE);
        final PropertiesManager pmgr = new PropertiesManager();

        Logger.putLog("Lanzando rastreo programado para la cuenta cliente " + cuentaCliente.getNombre(), ExecuteScheduledCrawling.class, Logger.LOG_LEVEL_INFO);
        try (Connection c = DataBaseManager.getConnection()) {
            final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, cuentaCliente.getDatosRastreo(), null, (long) 1);

            final List<DatosForm> users = LoginDAO.getClientAccount(c, cuentaCliente.getIdCuenta());

            final List<String> mailTo = getMailsFromRole(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.customer.user.id")), users);
            final List<String> mailToResponsibles = getMailsFromRole(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.customer.responsible.id")), users);

            cuentaCliente.getDatosRastreo().setCartuchos(CartuchoDAO.getNombreCartucho(cuentaCliente.getDatosRastreo().getId_rastreo()));
            cuentaCliente.getDatosRastreo().setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_SEMILLA, false));
            cuentaCliente.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_SEMILLA, true));
            cuentaCliente.getDatosRastreo().setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
            cuentaCliente.getDatosRastreo().setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
            cuentaCliente.getDatosRastreo().setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) cuentaCliente.getDatosRastreo().getId_rastreo()));
            if (CartuchoDAO.isCartuchoAccesibilidad(c, cuentaCliente.getDatosRastreo().getId_cartucho())) {
                cuentaCliente.getDatosRastreo().setFicheroNorma(CrawlerUtils.getFicheroNorma(cuentaCliente.getDatosRastreo().getId_guideline()));
            }
            final CrawlerData crawlerData = CrawlerUtils.getCrawlerData(cuentaCliente.getDatosRastreo(),
                    idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), mailTo, mailToResponsibles);
            RastreoDAO.actualizarFechaRastreo(c, crawlerData.getIdCrawling());

            // Lanzamos el hilo del rastreo
            SchedulingUtils.start(crawlerData);
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private List<String> getMailsFromRole(final Long idRole, final List<DatosForm> users) {
        final List<String> mails = new ArrayList<>();

        for (DatosForm userData : users) {
            for (Role role : userData.getRoles()) {
                if (role.getId().equals(idRole)) {
                    mails.add(userData.getEmail());
                }
            }
        }

        return mails;
    }

}
