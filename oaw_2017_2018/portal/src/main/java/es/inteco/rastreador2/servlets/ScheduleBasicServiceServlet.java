package es.inteco.rastreador2.servlets;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ScheduleBasicServiceServlet extends GenericServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Logger.putLog("Programando las peticiones al servicio básico", ScheduleBasicServiceServlet.class, Logger.LOG_LEVEL_INFO);

        final List<BasicServiceForm> basicServiceRequests = BasicServiceUtils.getBasicServiceRequestByStatus(Constants.BASIC_SERVICE_STATUS_SCHEDULED);
        try {
            if (basicServiceRequests != null) {
                for (BasicServiceForm basicServiceForm : basicServiceRequests) {
                    BasicServiceUtils.scheduleBasicServiceJob(basicServiceForm);
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente y observatorios", ScheduleBasicServiceServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
    }
}
