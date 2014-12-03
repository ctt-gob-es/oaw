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
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        CuentaCliente cuentaCliente = (CuentaCliente) jobDataMap.get(Constants.CUENTA_CLIENTE);

        Connection c = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();

            Logger.putLog("Lanzando rastreo programado para la cuenta cliente " + cuentaCliente.getNombre(), ExecuteScheduledCrawling.class, Logger.LOG_LEVEL_INFO);

            c = DataBaseManager.getConnection();
            Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, cuentaCliente.getDatosRastreo(), null, (long) 1);

            List<DatosForm> users = LoginDAO.getClientAccount(c, cuentaCliente.getIdCuenta());

            List<String> mailTo = getMailsFromRole(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.customer.user.id")), users);
            List<String> mailToResponsibles = getMailsFromRole(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.customer.responsible.id")), users);

            cuentaCliente.getDatosRastreo().setCartuchos(CartuchoDAO.getNombreCartucho(cuentaCliente.getDatosRastreo().getId_rastreo()));
            cuentaCliente.getDatosRastreo().setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_SEMILLA, false));
            cuentaCliente.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_SEMILLA, true));
            cuentaCliente.getDatosRastreo().setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
            cuentaCliente.getDatosRastreo().setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) cuentaCliente.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
            cuentaCliente.getDatosRastreo().setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) cuentaCliente.getDatosRastreo().getId_rastreo()));
            if (CartuchoDAO.isCartuchoAccesibilidad(c, cuentaCliente.getDatosRastreo().getId_cartucho())) {
                cuentaCliente.getDatosRastreo().setFicheroNorma(CrawlerUtils.getFicheroNorma(cuentaCliente.getDatosRastreo().getId_guideline()));
            }
            CrawlerData crawlerData = CrawlerUtils.getCrawlerData(cuentaCliente.getDatosRastreo(),
                    idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), mailTo, mailToResponsibles);
            RastreoDAO.updateRastreo(c, crawlerData.getIdCrawling());

            // Lanzamos el hilo del rastreo
            SchedulingUtils.start(crawlerData);
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

    }

    private List<String> getMailsFromRole(Long idRole, List<DatosForm> users) {
        List<String> mails = new ArrayList<String>();

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
