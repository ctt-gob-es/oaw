package es.inteco.crawler.servlet;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;

public class StopRunningCrawlingsServlet extends GenericServlet {

    /*
     * Puede que al detener el servidor hayan quedado rastreos activos. Los procesos se detendán al parar el servidor
     * pero en base de datos segirán constando como activos. Para evitarlo, cada vez que se reinicie el servidor
     * se actualizará el estado de los procesos activos a 'finalizado'.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try (Connection connection = DataBaseManager.getConnection()) {
            RastreoDAO.stopRunningCrawlings(connection);
            RastreoDAO.stopRunningObservatories(connection);
            Logger.putLog("Se ha cambiado el estado de los rastreos activos a 'finalizado'", StopRunningCrawlingsServlet.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("No se ha podido cambiar el estado de los rastreos activos a 'finalizado'", StopRunningCrawlingsServlet.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

    }

}
