package es.inteco.rastreador2.management;

import es.inteco.common.logging.Logger;

import javax.servlet.*;
import java.io.IOException;

public class ManagementServlet extends GenericServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ManagementThread managementThread = null;

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            Logger.putLog("Lanzando el hilo de monitorización de recursos", ManagementServlet.class, Logger.LOG_LEVEL_INFO);
            managementThread = new ManagementThread();
            managementThread.start();
        } catch (Exception e) {
            Logger.putLog("Error al lanzar el hilo de monitorización", ManagementServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void destroy() {
        if (managementThread != null) {
            // Paramos el thread
            managementThread.stopThread();
            managementThread = null;
        }
        super.destroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

}